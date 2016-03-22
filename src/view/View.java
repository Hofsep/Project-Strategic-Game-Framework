package view;

import model.*;

public interface View {
	
	/**
	 * Update to perform on notify view
	 * @param model
	 * @param arg
	 */
    public void update(Model model, Object arg);
}
