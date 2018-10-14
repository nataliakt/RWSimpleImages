package model;

import java.io.File;
import java.lang.reflect.Modifier;

public class EffectFile {
	
	private static final String ROOT = System.getProperty("user.dir").replace('\\', '/') + "/src/model/effect/";
	private static final String PACKAGE = "model.effect.";
	
	public static void load() {
		EffectList.getInstance().clear();
		File dirEffects = new File(ROOT);
		for (File file : dirEffects.listFiles()) {
			String className = file.getName().substring(0, file.getName().length() - 5);
			try {
				// Verifica se a classe não é abstrata
				if (!Modifier.isAbstract(Class.forName(PACKAGE + className).getModifiers())) {
					Effect e = (Effect) Class.forName(PACKAGE + className).newInstance();
					EffectList.getInstance().addEffect(e);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
}
