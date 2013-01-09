package dk.ihk.tcp.util.inject;

import dk.ihk.tcp.util.inject.Injector.Binding;

public abstract class Bindings
{
	protected Injector m_injector;

	public void setInjector(Injector injector) throws Exception
	{
		m_injector = injector;
		configure();
	}

	protected Binding bind(Class<?> clazz)
	{
		return m_injector.bind(clazz);
	}

	public abstract void configure() throws Exception;
}
