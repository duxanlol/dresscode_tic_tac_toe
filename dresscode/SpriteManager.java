package dresscode;

import javax.swing.ImageIcon;

public class SpriteManager {
	private  static SpriteManager instance = null;
	private static  ImageIcon xIcon,oIcon,emptyIcon,bottomMenuX,bottomMenuO, grid,bottomMenuWon,bottomMenuLost,bottomMenuDraw;

	public static SpriteManager getInstance() {
		if (instance == null)
			instance = new SpriteManager();
		return instance;
	}
	
	private SpriteManager() {
		this.emptyIcon =  (new ImageIcon(this.getClass().getResource("sprites/sprite_empty_new_with_boarder.png")));
		this.oIcon =  (new ImageIcon(this.getClass().getResource("sprites/sprite_o_with_border.png")));
		this.xIcon =  (new ImageIcon(this.getClass().getResource("sprites/sprite_x_with_border.png")));
		this.bottomMenuX =  (new ImageIcon(this.getClass().getResource("sprites/you_are_playing_as_x.png")));
		this.bottomMenuO =  (new ImageIcon(this.getClass().getResource("sprites/you_are_playing_as_o.png")));
		this.grid =  (new ImageIcon(this.getClass().getResource("sprites/board_with_borders.png")));
		this.bottomMenuWon = (new ImageIcon(this.getClass().getResource("sprites/bottom_menu_win.png")));
		this.bottomMenuLost = (new ImageIcon(this.getClass().getResource("sprites/bottom_menu_lost.png")));
		this.bottomMenuDraw =(new ImageIcon(this.getClass().getResource("sprites/bottom_menu_draw.png")));

	}

	public static ImageIcon getxIcon() {
		return xIcon;
	}

	public static ImageIcon getoIcon() {
		return oIcon;
	}

	public static ImageIcon getEmptyIcon() {
		return emptyIcon;
	}

	public static ImageIcon getBottomMenuX() {
		return bottomMenuX;
	}

	public static ImageIcon getBottomMenuO() {
		return bottomMenuO;
	}

	public static ImageIcon getGrid() {
		return grid;
	}

	public static ImageIcon getBottomMenuWon() {
		return bottomMenuWon;
	}

	public static ImageIcon getBottomMenuLost() {
		return bottomMenuLost;
	}

	public static ImageIcon getBottomMenuDraw() {
		return bottomMenuDraw;
	}
	
	
}
