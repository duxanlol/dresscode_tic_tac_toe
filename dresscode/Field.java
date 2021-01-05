package dresscode;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Field extends JButton {
	char value;
	ImageIcon currentIcon = null;
	int index;
	boolean isMouseover = false;
	boolean isPlayerOne = false;

	public Field() {
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		value = '0';
		currentIcon = SpriteManager.getInstance().getEmptyIcon();
	}

	public void update() {
		if (!isMouseover) {
			switch (value) {
			case '1':
				currentIcon = SpriteManager.getInstance().getxIcon();
				break;
			case '2':
				currentIcon = SpriteManager.getInstance().getoIcon();
				break;
			default:
				currentIcon = SpriteManager.getInstance().getEmptyIcon();
				break;
			}
		} else {
			if (isPlayerOne) {
				currentIcon = SpriteManager.getInstance().getxIcon();
			} else {
				currentIcon = SpriteManager.getInstance().getoIcon();
			}
		}
		setIcon(currentIcon);

	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void onMouseEnter(boolean isPlayerOne) {
		if (value == '0') {
			isMouseover = true;
			this.isPlayerOne = isPlayerOne;
			update();
		}
	}

	public void onMouseExit() {
		isMouseover = false;
		update();
	}
}
