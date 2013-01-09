package dk.ihk.tcp.util.inject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Add @Transient annotation to classes where a new instance should be
 * created every time the class is requested.
 * 
 * @author jacobpedersen
 * 
 */
public class Injector
{
	private final Map<Class<?>, Binding> m_bindings;
	private final Map<Class<?>, Object> m_instances;

	public Injector()
	{
		m_bindings = new HashMap<Class<?>, Binding>();
		m_instances = new HashMap<Class<?>, Object>();
	}

	public Injector(Bindings bindings) throws Exception
	{
		this();
		use(bindings);
	}

	public Binding bind(Class<?> clazz)
	{
		Binding b = new Binding(clazz);
		m_bindings.put(clazz, b);
		m_instances.remove(clazz);
		return b;
	}

	public void use(Bindings bindings) throws Exception
	{
		bindings.setInjector(this);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) throws Exception
	{
		Binding b = m_bindings.get(clazz);
		if (b != null)
		{
			if (m_instances.containsKey(clazz))
			{
				return (T) m_instances.get(clazz);
			}
			else if (b.m_providerClass != null)
			{
				if (!ensureDependencies(b))
				{
					throw new Exception("Missing dependencies for: " + b.m_clazz.toString());
				}
				T o = (T) create(b);
				m_instances.put(clazz, o);
				return o;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T create(Binding b) throws Exception
	{
		List<Object> args = new ArrayList<Object>();
		for (Class<?> clz : getDependencies(b.m_ctor))
		{
			args.add(m_instances.get(clz));
		}
		return (T) b.m_ctor.newInstance(args.toArray());
	}

	private boolean ensureDependencies(Binding b) throws Exception
	{
		Class<?>[] deps = getDependencies(b.m_ctor);
		for (Class<?> dep : deps)
		{
			if (!m_instances.containsKey(dep))
			{
				if (m_bindings.containsKey(dep))
				{
					Binding bb = m_bindings.get(dep);
					if (ensureDependencies(bb))
					{
						m_instances.put(dep, create(bb));
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
		}
		return true;
	}

	private Constructor<?> getInjectableConstructor(Class<?> clazz)
	{
		Constructor<?>[] constructors = clazz.getConstructors();
		for (Constructor<?> c : constructors)
		{
			if (c.isAnnotationPresent(Inject.class))
			{
				return c;
			}
		}

		return null;
	}

	private Class<?>[] getDependencies(Constructor<?> constructor)
	{
		return constructor.getParameterTypes();
	}

	public class Binding
	{
		public Class<?> m_clazz;
		public Constructor<?> m_ctor;
		public Class<?> m_providerClass;

		public Binding(Class<?> clazz)
		{
			m_clazz = clazz;
		}

		public void to(Class<?> clazz) throws Exception
		{
			m_providerClass = clazz;
			m_ctor = getInjectableConstructor(clazz);

			if (m_ctor == null)
			{
				throw new Exception("Construtor is missing an @Inject annotation: " + clazz.toString());
			}
		}

		public void to(Object o)
		{
			m_instances.put(m_clazz, o);
		}
	}
}
