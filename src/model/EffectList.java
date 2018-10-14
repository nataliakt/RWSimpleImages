package model;

import java.util.ArrayList;

public class EffectList {

	private ArrayList<Effect> effects;
	private static EffectList instance = null;

	public static EffectList getInstance() {
		if (instance == null) {
			instance = new EffectList();
		}
		return instance;
	}

	public EffectList() {
		super();
		effects = new ArrayList<Effect>();
	}

	public void clear() {
		effects.clear();
	}
	
	public void addEffect(Effect effect) {
		boolean add = false;
		for (int i = 0; i < effects.size(); i++) {
			if (!add && effects.get(i).getName().compareTo(effect.getName()) >= 0) {
				add = true;
				effects.add(i, effect);
			}
		}
		if (!add) {
			effects.add(effect);
		}
	}
	
	public void clearFilter() {
		effects.clear();
		EffectFile.load();
	}
	
	private void filterId(String id) {
		ArrayList<Effect> newEffects = (ArrayList<Effect>) effects.clone();
		for (Effect e : effects) {
			if (!id.contains(e.getFrom())) {
				
				if (e.getFrom().equals("PN")) {
					e.setFrom(id);
				} else if (e.getFrom().equals("PNC") && id.contains("C")) {
					e.setFrom(id);
				} else if (e.getFrom().equals("PN-C") && !id.contains("C")) {
					e.setFrom(id);
				} else {
					newEffects.remove(e);
				}
				
				if (e.getTo().equals("PN")) {
					e.setTo(id);
				} else if (e.getTo().equals("PNC")) {
					e.setTo(id.replace("C", "") + "C");
				} else {
					e.setTo(id.replace("C", ""));
				}
				
			} else if (id.contains("C")) {
				e.setTo(id);
			}
		}
		effects = newEffects;
	}
	
	public void filter(String name) {
		effects.clear();
		EffectFile.load();
		filterId(ImageList.getInstance().getSelected().getId());
		ArrayList<Effect> newEffects = (ArrayList<Effect>) effects.clone();
		for (Effect e : effects) {
			if (!e.getName().toLowerCase().contains(name.toLowerCase())) {
				newEffects.remove(e);
			}
		}
		effects = newEffects;
	}

	public Effect getEffect(int index) {
		return effects.get(index);
	}

	public ArrayList<Effect> getEffects() {
		return effects;
	}
}
