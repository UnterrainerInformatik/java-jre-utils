package info.unterrainer.commons.jreutils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ForName {

	/**
	 * Loads and instantiates a class given by fully-qualified name.
	 * 
	 * @param <T>                   the type of the class to instantiate
	 * @param fqn                   the fully-qualified name of the class to
	 *                              instantiate
	 * @param type                  the type of the class to instantiate
	 * @param constructorParameters a list of {@link ClassParam} containing the
	 *                              constructor parameters to use when instantiating
	 * @return the new instance
	 */
	public static <T> T instantiate(final String fqn, final Class<T> type, final ClassParam... constructorParameters) {
		Class<?> clazz = get(fqn);
		return instantiate(clazz, type, constructorParameters);
	}

	/**
	 * Loads and gets the handle to a class given a fully-qualified name.
	 * 
	 * @param fqn the fully-qualified name of the class to load
	 * @return the class
	 */
	public static Class<?> get(final String fqn) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fqn);
		} catch (ClassNotFoundException e) {
			log.warn("Could not load class of type [{}] by name", fqn, e);
		}
		return clazz;
	}

	/**
	 * Loads and instantiates a class.
	 * 
	 * @param <T>                   the type of the class to instantiate
	 * @param clazz                 the class to instantiate
	 * @param type                  the type of the class to instantiate
	 * @param constructorParameters a list of {@link ClassParam} containing the
	 *                              constructor parameters to use when instantiating
	 * @return the new instance
	 */
	@SuppressWarnings("unchecked")
	public static <T> T instantiate(final Class<?> clazz, final Class<T> type,
			final ClassParam... constructorParameters) {
		T instance = null;
		int l = 0;
		if (constructorParameters.length != 1 || constructorParameters[0] != null)
			l = constructorParameters.length;
		Class<?>[] constructorParameterTypes = new Class<?>[l];
		Object[] constructorParameterInstances = new Object[l];
		for (int i = 0; i < l; i++) {
			ClassParam p = constructorParameters[i];
			constructorParameterTypes[i] = p.getClazz();
			constructorParameterInstances[i] = p.getInstance();
		}

		try {
			instance = (T) clazz.getConstructor(constructorParameterTypes).newInstance(constructorParameterInstances);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.warn("Error instantiating [{}]. Could not find constructor({}).", clazz.getName(),
					String.join(", ",
							Arrays.asList(constructorParameterTypes)
									.stream()
									.map(Class::getSimpleName)
									.collect(Collectors.toList())));
			return null;
		}
		if (!type.isAssignableFrom(instance.getClass())) {
			log.warn("Error loading new [{}]. The class [{}] is not of type, or a subclass of [{}].", type,
					clazz.getName(), type);
			return null;
		}
		return instance;
	}
}
