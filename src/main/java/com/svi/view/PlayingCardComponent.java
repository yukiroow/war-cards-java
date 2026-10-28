package com.svi.view;

import com.svi.model.Card;
import com.svi.model.cardenum.Rank;
import com.svi.model.cardenum.Suit;

import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * A flat, resolution-independent playing card. Everything is drawn relative to the
 * component's current width, so it scales to whatever cell the layout gives it.
 */
public class PlayingCardComponent extends JComponent {

    private final Card card;

    // Hints only: the card grid sets the real size. 60 x 84 keeps the 5:7 card ratio.
    private static final int PREFERRED_WIDTH = 60;
    private static final int PREFERRED_HEIGHT = 84;
    private static final int MINIMUM_WIDTH = 24;
    private static final int MINIMUM_HEIGHT = 34;

    private static final Color FACE_BORDER = new Color(203, 210, 220);
    private static final Color EMPTY_OUTLINE = new Color(128, 138, 152, 140);
    private static final Color RED_SUIT = new Color(220, 38, 38);
    private static final Color BLACK_SUIT = new Color(30, 41, 59);

    private static final Font RANK_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 1);
    private static final Font SUIT_FONT = new Font(Font.SERIF, Font.PLAIN, 1);

    public PlayingCardComponent(Card card) {
        this.card = card;
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
        setToolTipText(card == null ? "Empty card" : card.toString());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int w = getWidth();
        int h = getHeight();
        if (w < 2 || h < 2) {
            return;
        }

        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        // The face fills the whole component; the 0.5 offset keeps the 1px outline inside the bounds.
        float arc = Math.max(8f, w * 0.2f);
        RoundRectangle2D face = new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f, arc, arc);

        if (card == null) {
            // Empty slot: dashed outline, no fill.
            g.setColor(EMPTY_OUTLINE);
            g.setStroke(new BasicStroke(
                    1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{4f, 3f}, 0f
            ));
            g.draw(face);
            g.dispose();
            return;
        }

        g.setColor(Color.WHITE);
        g.fill(face);
        g.setColor(FACE_BORDER);
        g.setStroke(new BasicStroke(1f));
        g.draw(face);

        String rank = rankSymbol(card.getRank());
        String suit = suitSymbol(card.getSuit());
        g.setColor(suitColor(card.getSuit()));

        paintCenterSuit(g, suit, w, h);

        // Top-left index.
        paintIndex(g, rank, suit, w);

        // Bottom-right index: same index rotated 180 degrees around the card center.
        Graphics2D flipped = (Graphics2D) g.create();
        flipped.rotate(Math.PI, w / 2.0, h / 2.0);
        paintIndex(flipped, rank, suit, w);
        flipped.dispose();

        g.dispose();
    }

    /** Rank over a small suit, centered on one vertical line near the corner. */
    private void paintIndex(Graphics2D g, String rank, String suit, int w) {
        float rankSize = w * 0.27f;
        float suitSize = w * 0.22f;
        float centerX = w * 0.21f;
        float rankBaseline = w * 0.09f + rankSize * 0.72f;
        float suitBaseline = rankBaseline + suitSize;

        drawCentered(g, rank, RANK_FONT.deriveFont(rankSize), centerX, rankBaseline);
        drawCentered(g, suit, SUIT_FONT.deriveFont(suitSize), centerX, suitBaseline);
    }

    /** Large suit, centered using the glyph's real outline rather than font metrics. */
    private void paintCenterSuit(Graphics2D g, String suit, int w, int h) {
        Font font = SUIT_FONT.deriveFont(w * 0.46f);
        GlyphVector glyphs = font.createGlyphVector(g.getFontRenderContext(), suit);
        Rectangle2D bounds = glyphs.getVisualBounds();
        g.drawGlyphVector(
                glyphs,
                (float) (w / 2.0 - bounds.getCenterX()),
                (float) (h / 2.0 - bounds.getCenterY())
        );
    }

    private void drawCentered(Graphics2D g, String text, Font font, float centerX, float baseline) {
        g.setFont(font);
        float width = (float) font.getStringBounds(text, g.getFontRenderContext()).getWidth();
        g.drawString(text, centerX - width / 2f, baseline);
    }

    private String rankSymbol(Rank rank) {
        return switch (rank.name()) {
            case "A" -> "A";
            case "K" -> "K";
            case "Q" -> "Q";
            case "J" -> "J";
            case "TEN" -> "10";
            default -> String.valueOf(rank.getValue());
        };
    }

    private String suitSymbol(Suit suit) {
        return switch (suit.name()) {
            case "H" -> "♥";
            case "D" -> "♦";
            case "C" -> "♣";
            case "S" -> "♠";
            default -> "?";
        };
    }

    private Color suitColor(Suit suit) {
        return switch (suit.name()) {
            case "H", "D" -> RED_SUIT;
            case "C", "S" -> BLACK_SUIT;
            default -> Color.GRAY;
        };
    }
}