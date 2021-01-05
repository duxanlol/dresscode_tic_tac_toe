package dresscode;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import java.awt.Color;
import javax.swing.JButton;

public class GUI extends JFrame implements ActionListener {

	JLabel gameGridBackground, bottomMenuBackground;
	boolean isPlayerOne, isOver;
	Field[] fields = new Field[16];
	Random random;
	State state;
	DecisionEngine de;
	private JButton exitButton, resetButton;

	public GUI() {
		super("Dresscode 4x4 Tic Tac Toe");
		random = new Random();
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setUndecorated(true);
		setSize(512 + 15, 640 + 25); // +15 +25 to adjust for Window borders and title bar.
		displayGame();
		resetGame();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public void initFields() {
		int xCordStart = 8;
		int yCordStart = 8;
		int width = 112;
		int height = 112;
		for (int i = 0; i < 16; i++) {
			fields[i] = new Field();
			fields[i].setIndex(i);
			fields[i].setBounds(xCordStart, yCordStart, width, height);
			getContentPane().add(fields[i]);
			xCordStart += 128;
			if (i % 4 == 3) {
				xCordStart = 8;
				yCordStart += 128;
			}
			fields[i].addActionListener(this);
			Field curButton = fields[i];
			curButton.addMouseListener(new MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	if(!isOver) {
			    	curButton.onMouseEnter(isPlayerOne);
			    	}
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
			    	curButton.onMouseExit();
			    }
			});
			fields[i].update();
		}
	}

	public void displayGame() {
		getContentPane().setLayout(null);
		gameGridBackground = new JLabel(SpriteManager.getInstance().getGrid()); 																														// ImageIcon(this.getClass().getResource("sprites/board_with_borders.png")));
		gameGridBackground.setBounds(0, 0, 512, 512);
		bottomMenuBackground = new JLabel();
		bottomMenuBackground.setBounds(0, 512, 512, 128);
		getContentPane().add(gameGridBackground);
		getContentPane().add(bottomMenuBackground);
		initFields();
		gameGridBackground.setLayout(null);
		bottomMenuBackground.setLayout(null);

		resetButton = new JButton("");
		resetButton.setBounds(236, 586, 132, 39);
		resetButton.setBorderPainted(false);
		resetButton.setFocusPainted(false);
		resetButton.setContentAreaFilled(false);
		resetButton.addActionListener(this);
		getContentPane().add(resetButton);

		exitButton = new JButton("");
		exitButton.setBounds(393, 586, 100, 39);
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.addActionListener(this);
		getContentPane().add(exitButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Field) {
			Field clicked = (Field) e.getSource();
			onFieldClick(clicked.getIndex());
		}else if (e.getSource().equals(resetButton)) {
			resetGame();
		}else if (e.getSource().equals(exitButton)) {
			System.exit(0);
		}
	}

	public void onFieldClick(int index) {
		if(isOver) return;
		boolean playerOnePlaysNext = false;
		if (state.getTurnNumber() % 2 == 0) {
			playerOnePlaysNext = true;
		}

		if (isPlayerOne == playerOnePlaysNext) {
			if (state.playMove(index)) {
				update(state);
				if (state.hasVictor(index)) {
					finishedGame("won");
				} else if (state.getTurnNumber() == 16) {
					finishedGame("draw");
				} else {
					index = de.findMaxValuePlay(state.toString());
					state.playMove(index);
					update(state);
					if (state.hasVictor(index)) {
						finishedGame("lost");
					} else if (state.getTurnNumber() == 16) {
						finishedGame("draw");
					}
				}
			}
		}
	}

	public void resetGame() {
		state = new State();
		isPlayerOne = random.nextBoolean();
		de = new DecisionEngine();
		if (isPlayerOne) {
			bottomMenuBackground.setIcon(SpriteManager.getInstance().getBottomMenuX());
		}else {
			bottomMenuBackground.setIcon(SpriteManager.getInstance().getBottomMenuO());
			state.playMove(de.findMaxValuePlay(state.toString()));
		}
		update(state);
		isOver = false;
	}

	public void update(State state) {
		String stateString = state.toString();
		for (int i = 0; i < 16; i++) {
			fields[i].setValue(stateString.charAt(i));
			fields[i].update();
		}
	}

	public void finishedGame(String message) {
		isOver = true;
		if (message.equalsIgnoreCase("won")) {
			bottomMenuBackground.setIcon(SpriteManager.getInstance().getBottomMenuWon());
		} else if (message.equalsIgnoreCase("lost")) {
			bottomMenuBackground.setIcon(SpriteManager.getInstance().getBottomMenuLost());
		} else {
			bottomMenuBackground.setIcon(SpriteManager.getInstance().getBottomMenuDraw());
		}

	}

}