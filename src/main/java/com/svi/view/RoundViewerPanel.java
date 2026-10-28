package com.svi.view;

import com.svi.model.Card;
import com.svi.model.Deck;
import com.svi.model.Player;
import com.svi.model.Round;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class RoundViewerPanel extends JPanel {

    // Fixed window size: the panel always asks for exactly 800 x 500.
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 500;
    private static final int SIDE_PADDING = 16;

    // Cards are sized from the available width, so a 52-card deck is 4 rows of 13
    // and fits without scrolling. Round pool uses fewer, larger cards.
    private static final int DECK_COLUMNS = 13;
    private static final int POOL_COLUMNS = 8;
    private static final int CARD_GAP = 6;
    private static final double CARD_ASPECT = 1.4; // card height / card width
    private static final int PLAYER_LABEL_HEIGHT = 18;

    // No background fills anywhere. Only accent, muted text and hairlines are colored.
    private static final Color ACCENT = new Color(37, 99, 235);
    private static final Color TEXT_MUTED = new Color(115, 125, 140);
    private static final Color LINE = new Color(128, 138, 152, 90);
    private static final Color POPUP_BORDER = new Color(203, 213, 225);

    private final ArrayList<Round> gameHistory;
    private final Deck originalDeck;
    private final Deck shuffledDeck;

    private final JLabel roundLabel;
    private final PillLabel winnerPill;
    private final JComboBox<Round> roundSelector;
    private final JTabbedPane tabs;

    private final CardGrid originalDeckPanel;
    private final CardGrid shuffledDeckPanel;
    private final CardGrid roundPoolPanel;
    private final CardGrid winnerDeckPanel;

    private final JScrollPane originalDeckScroll;
    private final JScrollPane shuffledDeckScroll;
    private final JScrollPane roundPoolScroll;
    private final JScrollPane winnerDeckScroll;

    public RoundViewerPanel(ArrayList<Round> gameHistory, Deck originalDeck, Deck shuffledDeck) {
        this.gameHistory = gameHistory;
        this.originalDeck = originalDeck;
        this.shuffledDeck = shuffledDeck;

        Dimension fixedSize = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        setOpaque(false);
        setLayout(new BorderLayout(0, 8));
        setBorder(new EmptyBorder(16, SIDE_PADDING, 12, SIDE_PADDING));

        // Header: round title and winner on the left, round selector on the right.
        roundLabel = new JLabel("Select a round");
        roundLabel.setForeground(primaryText());
        roundLabel.setFont(font(Font.BOLD, 20f));

        winnerPill = new PillLabel();
        winnerPill.setFont(font(Font.BOLD, 12f));
        winnerPill.setVisible(false);

        JPanel title = new JPanel();
        title.setOpaque(false);
        title.setLayout(new BoxLayout(title, BoxLayout.X_AXIS));
        title.add(roundLabel);
        title.add(Box.createHorizontalStrut(12));
        title.add(winnerPill);

        roundSelector = new JComboBox<>(gameHistory.toArray(new Round[0]));
        styleRoundSelector();

        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        header.add(title, BorderLayout.CENTER);
        header.add(roundSelector, BorderLayout.EAST);

        // Card grids.
        originalDeckPanel = new CardGrid(DECK_COLUMNS, 0);
        shuffledDeckPanel = new CardGrid(DECK_COLUMNS, 0);
        roundPoolPanel = new CardGrid(POOL_COLUMNS, PLAYER_LABEL_HEIGHT);
        winnerDeckPanel = new CardGrid(DECK_COLUMNS, 0);

        originalDeckScroll = createDeckScrollPane(originalDeckPanel);
        shuffledDeckScroll = createDeckScrollPane(shuffledDeckPanel);
        roundPoolScroll = createDeckScrollPane(roundPoolPanel);
        winnerDeckScroll = createDeckScrollPane(winnerDeckPanel);

        // Flat tabs: text only, accent underline on the selected tab.
        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setUI(new FlatTabbedPaneUI());
        tabs.setOpaque(false);
        tabs.setBorder(null);
        tabs.setFont(font(Font.PLAIN, 13f));
        tabs.setFocusable(false);
        tabs.addTab("Original deck", originalDeckScroll);
        tabs.addTab("Shuffled deck", shuffledDeckScroll);
        tabs.addTab("Played this round", roundPoolScroll);
        tabs.addTab("Winner's deck", winnerDeckScroll);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        roundSelector.addActionListener(e -> {
            Round selected = (Round) roundSelector.getSelectedItem();
            if (selected != null) {
                displayRound(selected);
            }
        });

        displayGameDecks();
        if (!gameHistory.isEmpty()) {
            roundSelector.setSelectedIndex(0);
            displayRound(gameHistory.get(0));
        } else {
            roundLabel.setText("No rounds played");
            tabs.setSelectedComponent(roundPoolScroll);
        }
    }

    // ------------------------------------------------------------------
    // Styling helpers
    // ------------------------------------------------------------------

    private static Color primaryText() {
        Color c = UIManager.getColor("Label.foreground");
        return c != null ? c : new Color(30, 41, 59);
    }

    private static Font font(int style, float size) {
        Font base = UIManager.getFont("Label.font");
        if (base == null) {
            base = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        }
        return base.deriveFont(style, size);
    }

    private static Graphics2D smooth(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2;
    }

    private static void styleScrollBar(JScrollBar bar) {
        bar.setUI(new ThinScrollBarUI());
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(10, 0));
    }

    private void styleRoundSelector() {
        roundSelector.setUI(new FlatComboBoxUI());
        roundSelector.setOpaque(false);
        roundSelector.setBorder(new RoundedBorder(LINE, 10));
        roundSelector.setFont(font(Font.PLAIN, 13f));
        roundSelector.setForeground(primaryText());
        roundSelector.setFocusable(false);
        roundSelector.setPreferredSize(new Dimension(124, 30));
        roundSelector.setRenderer(new DefaultListCellRenderer() {
            private final EmptyBorder popupPadding = new EmptyBorder(6, 12, 6, 12);
            private final EmptyBorder fieldPadding = new EmptyBorder(0, 12, 0, 0);

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                boolean inPopup = index >= 0;
                setFont(font(Font.PLAIN, 13f));
                setBorder(inPopup ? popupPadding : fieldPadding);
                setOpaque(inPopup);
                if (inPopup) {
                    setForeground(isSelected ? Color.WHITE : list.getForeground());
                    setBackground(isSelected ? ACCENT : list.getBackground());
                } else {
                    setForeground(primaryText());
                }
                if (value instanceof Round round) {
                    setText("Round " + round.getRoundNo());
                }
                return this;
            }
        });
    }

    private JScrollPane createDeckScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(
                panel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        // Simple scrolling avoids smearing artifacts on a transparent viewport.
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        styleScrollBar(scrollPane.getVerticalScrollBar());
        scrollPane.setMinimumSize(new Dimension(0, 0));
        return scrollPane;
    }

    // ------------------------------------------------------------------
    // Content
    // ------------------------------------------------------------------

    private void displayGameDecks() {
        originalDeckPanel.removeAll();
        shuffledDeckPanel.removeAll();

        if (originalDeck != null && originalDeck.cards() != null) {
            for (Card card : originalDeck.cards()) {
                originalDeckPanel.add(new PlayingCardComponent(card));
            }
        }
        if (shuffledDeck != null && shuffledDeck.cards() != null) {
            for (Card card : shuffledDeck.cards()) {
                shuffledDeckPanel.add(new PlayingCardComponent(card));
            }
        }

        refreshCardGrid(originalDeckPanel);
        refreshCardGrid(shuffledDeckPanel);
    }

    private void displayRound(Round round) {
        if (round == null) {
            return;
        }

        Player winner = round.getWinner();
        roundLabel.setText("Round " + round.getRoundNo());
        if (winner == null) {
            winnerPill.setText("No winner");
            winnerPill.setForeground(TEXT_MUTED);
        } else {
            winnerPill.setText("Winner: Player " + winner.getPlayerNo());
            winnerPill.setForeground(ACCENT);
        }
        winnerPill.setVisible(true);

        roundPoolPanel.removeAll();
        if (round.getRoundPool() != null) {
            for (Map.Entry<Player, Card> entry : round.getRoundPool().entrySet()) {
                Player player = entry.getKey();
                Card card = entry.getValue();

                JPanel playedCard = new JPanel(new BorderLayout());
                playedCard.setOpaque(false);

                JLabel playerLabel = new JLabel("Player " + player.getPlayerNo(), SwingConstants.CENTER);
                playerLabel.setFont(font(Font.BOLD, 11f));
                playerLabel.setForeground(player.equals(winner) ? ACCENT : TEXT_MUTED);
                playerLabel.setPreferredSize(new Dimension(10, PLAYER_LABEL_HEIGHT));

                playedCard.add(playerLabel, BorderLayout.NORTH);
                playedCard.add(new PlayingCardComponent(card), BorderLayout.CENTER);
                roundPoolPanel.add(playedCard);
            }
        }

        winnerDeckPanel.removeAll();
        if (winner != null && winner.getDeck() != null && winner.getDeck().cards() != null) {
            for (Card card : winner.getDeck().cards()) {
                winnerDeckPanel.add(new PlayingCardComponent(card));
            }
        }

        refreshCardGrid(roundPoolPanel);
        refreshCardGrid(winnerDeckPanel);
    }

    private void refreshCardGrid(JPanel panel) {
        panel.revalidate();
        panel.repaint();

        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(
                JScrollPane.class, panel
        );
        if (scrollPane != null) {
            scrollPane.revalidate();
            scrollPane.repaint();
        }
        revalidate();
        repaint();
    }

    // ------------------------------------------------------------------
    // Card grid: fixed columns, cards scale to the width, fixed aspect ratio
    // ------------------------------------------------------------------

    private static final class CardGrid extends JPanel implements Scrollable {
        private static final int DEFAULT_WIDTH = WINDOW_WIDTH - 2 * SIDE_PADDING;

        private final int columns;
        private final int extraCellHeight;

        CardGrid(int columns, int extraCellHeight) {
            super(null);
            this.columns = columns;
            this.extraCellHeight = extraCellHeight;
            setOpaque(false);
            setBorder(new EmptyBorder(12, 0, 4, 0));
        }

        private int cellWidth(int totalWidth) {
            Insets in = getInsets();
            int inner = totalWidth - in.left - in.right;
            return Math.max(24, (inner - (columns - 1) * CARD_GAP) / columns);
        }

        private int cellHeight(int cellWidth) {
            return (int) Math.round(cellWidth * CARD_ASPECT) + extraCellHeight;
        }

        @Override
        public void doLayout() {
            Insets in = getInsets();
            int cw = cellWidth(getWidth());
            int ch = cellHeight(cw);
            int gridWidth = columns * cw + (columns - 1) * CARD_GAP;
            int x0 = in.left + Math.max(0, (getWidth() - in.left - in.right - gridWidth) / 2);

            int index = 0;
            for (Component child : getComponents()) {
                int col = index % columns;
                int row = index / columns;
                child.setBounds(x0 + col * (cw + CARD_GAP), in.top + row * (ch + CARD_GAP), cw, ch);
                index++;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            int width = getParent() instanceof JViewport viewport && viewport.getWidth() > 0
                    ? viewport.getWidth()
                    : DEFAULT_WIDTH;
            int cw = cellWidth(width);
            int ch = cellHeight(cw);
            int rows = (getComponentCount() + columns - 1) / columns;
            Insets in = getInsets();
            int gridHeight = rows == 0 ? 0 : rows * ch + (rows - 1) * CARD_GAP;
            return new Dimension(width, in.top + gridHeight + in.bottom);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return cellHeight(cellWidth(Math.max(getWidth(), 1))) / 2;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return Math.max(visibleRect.height - 20, 20);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    // ------------------------------------------------------------------
    // Small custom components
    // ------------------------------------------------------------------

    /** Label with a thin rounded outline in its foreground color. */
    private static final class PillLabel extends JLabel {
        PillLabel() {
            setOpaque(false);
            setBorder(new EmptyBorder(3, 12, 3, 12));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = smooth(g);
            Color fg = getForeground();
            g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 130));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = smooth(g);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
    }

    // ------------------------------------------------------------------
    // Look and feel for tabs, combo box and scroll bar
    // ------------------------------------------------------------------

    /** Text-only tabs with an accent underline on the selected one. No boxes, no slanted edges. */
    private static final class FlatTabbedPaneUI extends BasicTabbedPaneUI {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            tabInsets = new Insets(6, 16, 8, 16);
            selectedTabPadInsets = new Insets(0, 0, 0, 0);
            tabAreaInsets = new Insets(0, 0, 0, 0);
            contentBorderInsets = new Insets(0, 0, 0, 0);
        }

        @Override
        protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
            // Hairline under the whole tab strip.
            if (tabPane.getTabCount() > 0 && rects.length > 0 && rects[0] != null) {
                Rectangle first = rects[0];
                g.setColor(LINE);
                g.fillRect(0, first.y + first.height - 1, tabPane.getWidth(), 1);
            }
            super.paintTabArea(g, tabPlacement, selectedIndex);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                          int x, int y, int w, int h, boolean isSelected) {
            // Intentionally empty: no tab fill.
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                      int x, int y, int w, int h, boolean isSelected) {
            if (isSelected) {
                g.setColor(ACCENT);
                g.fillRect(x, y + h - 2, w, 2);
            }
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // Intentionally empty: no frame around the tab content.
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects,
                                           int tabIndex, Rectangle iconRect, Rectangle textRect,
                                           boolean isSelected) {
            // Intentionally empty: no dotted focus box.
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
                                 int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            Graphics2D g2 = smooth(g);
            g2.setFont(font);
            g2.setColor(isSelected ? primaryText() : TEXT_MUTED);
            g2.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            g2.dispose();
        }

        @Override
        protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
            return 0;
        }

        @Override
        protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
            return 0;
        }
    }

    private static final class FlatComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton arrow = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = smooth(g);
                    g2.setColor(TEXT_MUTED);
                    g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int cx = getWidth() / 2;
                    int cy = getHeight() / 2;
                    g2.drawPolyline(
                            new int[]{cx - 4, cx, cx + 4},
                            new int[]{cy - 2, cy + 2, cy - 2},
                            3
                    );
                    g2.dispose();
                }
            };
            arrow.setBorder(null);
            arrow.setOpaque(false);
            arrow.setContentAreaFilled(false);
            arrow.setFocusPainted(false);
            arrow.setFocusable(false);
            return arrow;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Intentionally empty: no fill behind the selected value.
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane scroller = super.createScroller();
                    scroller.setBorder(null);
                    styleScrollBar(scroller.getVerticalScrollBar());
                    return scroller;
                }
            };
            popup.setBorder(new LineBorder(POPUP_BORDER, 1));
            return popup;
        }
    }

    /** Slim rounded scroll thumb, no arrow buttons, no track. */
    private static final class ThinScrollBarUI extends BasicScrollBarUI {
        private static final Color THUMB = new Color(128, 138, 152, 110);

        private static JButton emptyButton() {
            JButton button = new JButton();
            Dimension zero = new Dimension(0, 0);
            button.setPreferredSize(zero);
            button.setMinimumSize(zero);
            button.setMaximumSize(zero);
            return button;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return emptyButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return emptyButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            // Intentionally empty: no track.
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.width <= 0 || thumbBounds.height <= 0) {
                return;
            }
            Graphics2D g2 = smooth(g);
            g2.setColor(THUMB);
            int arc = Math.min(thumbBounds.width, thumbBounds.height) - 4;
            g2.fillRoundRect(
                    thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4,
                    arc, arc
            );
            g2.dispose();
        }
    }
}