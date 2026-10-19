
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

public class StringMatching extends JFrame {

    private final Color bg = new Color(12, 16, 35);
    private final Color panel = new Color(24, 30, 57);
    private final Color cyan = new Color(55, 225, 255);
    private final Color purple = new Color(164, 105, 255);
    private final Color green = new Color(75, 235, 160);
    private final Color muted = new Color(165, 180, 210);

    private JTextField textField;
    private JTextField patternField;
    private JTextPane previewPane;
    private JLabel naiveResult;
    private JLabel rkResult;
    private JLabel totalResult;
    private JLabel statusLabel;
    private JLabel naiveTime;
    private JLabel rkTime;
    private JProgressBar progress;
    private Timer animationTimer;
    private float animation = 0f;

    public StringMatching() {
        setTitle("String Matching | Algorithm Studio");
        setSize(1050, 820);
        setMinimumSize(new Dimension(750, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(20, 24, 18, 24));
        setContentPane(root);

        // Animated gradient header
        GradientHeader header = new GradientHeader();
        header.setPreferredSize(new Dimension(900, 135));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(23, 27, 20, 25));

        JPanel headerText = new JPanel();
        headerText.setOpaque(false);
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));

        JLabel eyebrow = new JLabel("ALGORITHM STUDIO   /   JAVA PROJECT");
        eyebrow.setForeground(cyan);
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 11));

        JLabel heading = new JLabel("STRING MATCHING");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("SansSerif", Font.BOLD, 30));

        JLabel subtitle = new JLabel(
                "Discover patterns. Compare algorithms. Visualize results.");
        subtitle.setForeground(new Color(222, 230, 255));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        headerText.add(eyebrow);
        headerText.add(Box.createVerticalStrut(8));
        headerText.add(heading);
        headerText.add(Box.createVerticalStrut(5));
        headerText.add(subtitle);

        header.add(headerText, BorderLayout.CENTER);

        JLabel badge = new JLabel("  ●  LIVE SEARCH  ");
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(150, 205, 255, 130)),
                new EmptyBorder(8, 7, 8, 7)));
        header.add(badge, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Input card
        JPanel inputCard = card("01    INPUT TEXT & PATTERN");
        inputCard.setLayout(new BorderLayout(12, 12));

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);

        textField = createInput("ABCDABCDABCD");
        patternField = createInput("ABCD");

        addField(fields, 0, "YOUR TEXT", textField);
        addField(fields, 1, "SEARCH PATTERN", patternField);

        inputCard.add(fields, BorderLayout.CENTER);

        RoundedButton searchButton = new RoundedButton(
                "▶   Search Pattern", cyan, new Color(15, 155, 205));
        RoundedButton clearButton = new RoundedButton(
                "↺   Clear", new Color(64, 72, 105),
                new Color(44, 50, 78));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(searchButton);
        buttonRow.add(clearButton);

        progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setForeground(cyan);
        progress.setBackground(panel);
        progress.setPreferredSize(new Dimension(100, 8));
        progress.setVisible(false);

        buttonRow.add(progress);
        inputCard.add(buttonRow, BorderLayout.SOUTH);

        content.add(inputCard);
        content.add(Box.createVerticalStrut(15));

        // Preview card
        JPanel previewCard = card("02    LIVE TEXT PREVIEW");

        JLabel previewHint = new JLabel(
                "Matching substrings are highlighted in green.");
        previewHint.setForeground(muted);
        previewHint.setFont(new Font("SansSerif", Font.PLAIN, 12));

        previewPane = new JTextPane();
        previewPane.setEditable(false);
        previewPane.setFont(new Font("Monospaced", Font.PLAIN, 16));
        previewPane.setBackground(new Color(13, 19, 40));
        previewPane.setForeground(new Color(230, 238, 255));
        previewPane.setBorder(new EmptyBorder(16, 15, 16, 15));

        JScrollPane previewScroll = new JScrollPane(previewPane);
        previewScroll.setBorder(BorderFactory.createLineBorder(
                new Color(64, 79, 120)));
        previewScroll.setPreferredSize(new Dimension(900, 105));

        previewCard.add(previewHint, BorderLayout.NORTH);
        previewCard.add(Box.createVerticalStrut(10), BorderLayout.WEST);
        previewCard.add(previewScroll, BorderLayout.CENTER);

        content.add(previewCard);
        content.add(Box.createVerticalStrut(15));

        // Results card
        JPanel resultsCard = card("03    PERFORMANCE DASHBOARD");
        resultsCard.setLayout(new BorderLayout(10, 12));

        JPanel resultCards = new JPanel(new GridLayout(1, 2, 12, 0));
        resultCards.setOpaque(false);

        JPanel naiveCard = metricCard(
                "NAIVE STRING MATCHING", cyan);
        JPanel rkCard = metricCard(
                "RABIN-KARP ALGORITHM", purple);

        naiveResult = metricValue(naiveCard);
        rkResult = metricValue(rkCard);
        naiveTime = metricTime(naiveCard);
        rkTime = metricTime(rkCard);

        resultCards.add(naiveCard);
        resultCards.add(rkCard);

        resultsCard.add(resultCards, BorderLayout.CENTER);

        JPanel bottomResults = new JPanel(new BorderLayout(8, 0));
        bottomResults.setOpaque(false);

        totalResult = new JLabel("TOTAL MATCHES     —");
        totalResult.setForeground(green);
        totalResult.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel complexity = new JLabel("Worst case: O(n × m)");
        complexity.setForeground(muted);
        complexity.setFont(new Font("SansSerif", Font.PLAIN, 12));

        bottomResults.add(totalResult, BorderLayout.WEST);
        bottomResults.add(complexity, BorderLayout.EAST);

        resultsCard.add(bottomResults, BorderLayout.SOUTH);

        content.add(resultsCard);

        JScrollPane contentScroll = new JScrollPane(content);
        contentScroll.setBorder(null);
        contentScroll.setOpaque(false);
        contentScroll.getViewport().setOpaque(false);
        contentScroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(contentScroll, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 2, 0, 2));

        statusLabel = new JLabel("●  SYSTEM READY");
        statusLabel.setForeground(green);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 11));

        JLabel footerRight = new JLabel("JAVA  •  SWING  •  STRING SEARCH");
        footerRight.setForeground(muted);
        footerRight.setFont(new Font("SansSerif", Font.PLAIN, 10));

        footer.add(statusLabel, BorderLayout.WEST);
        footer.add(footerRight, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);

        // Header animation
        animationTimer = new Timer(45, e -> {
            animation += 0.012f;
            if (animation > 1f) {
                animation = 0f;
            }
            header.setPhase(animation);
        });
        animationTimer.start();

        // Search animation, then calculate results
        searchButton.addActionListener(e -> {
            String text = textField.getText();
            String pattern = patternField.getText();

            if (pattern.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Please enter a search pattern.");
                patternField.requestFocus();
                return;
            }

            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Please enter some text.");
                textField.requestFocus();
                return;
            }

            searchButton.setEnabled(false);
            progress.setVisible(true);
            statusLabel.setText("●  ANALYZING PATTERN...");
            statusLabel.setForeground(cyan);

            Timer searchTimer = new Timer(350, ev -> {
                ((Timer) ev.getSource()).stop();

                long start = System.nanoTime();
                List<Integer> naive = naiveSearch(text, pattern);
                long naiveElapsed = System.nanoTime() - start;

                start = System.nanoTime();
                List<Integer> rk = rabinKarp(text, pattern);
                long rkElapsed = System.nanoTime() - start;

                showPreview(text, pattern, naive);

                naiveResult.setText(naive.toString());
                rkResult.setText(rk.toString());
                naiveTime.setText(
                        "Execution: " + naiveElapsed + " ns");
                rkTime.setText(
                        "Execution: " + rkElapsed + " ns");

                totalResult.setText(
                        "TOTAL MATCHES     " + naive.size());

                statusLabel.setText(
                        naive.equals(rk)
                                ? "●  SEARCH COMPLETED  •  RESULTS VERIFIED"
                                : "●  WARNING  •  RESULTS DIFFER");
                statusLabel.setForeground(
                        naive.equals(rk) ? green : Color.ORANGE);

                progress.setVisible(false);
                searchButton.setEnabled(true);
                root.repaint();
            });

            searchTimer.setRepeats(false);
            searchTimer.start();
        });

        clearButton.addActionListener(e -> {
            textField.setText("");
            patternField.setText("");
            previewPane.setText("");

            naiveResult.setText("—");
            rkResult.setText("—");
            naiveTime.setText("Execution: —");
            rkTime.setText("Execution: —");
            totalResult.setText("TOTAL MATCHES     —");

            statusLabel.setText("●  SYSTEM READY");
            statusLabel.setForeground(green);
            textField.requestFocus();
        });

        showPreview(textField.getText(), patternField.getText(),
                naiveSearch(textField.getText(), patternField.getText()));
        naiveResult.setText("—");
        rkResult.setText("—");
    }

    private JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(panel);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(58, 68, 105)),
                new EmptyBorder(16, 17, 16, 17)));

        JLabel label = new JLabel(title);
        label.setForeground(new Color(230, 235, 255));
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        p.add(label, BorderLayout.NORTH);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JTextField createInput(String value) {
        JTextField field = new JTextField(value, 30);
        field.setFont(new Font("Monospaced", Font.PLAIN, 14));
        field.setBackground(new Color(13, 19, 40));
        field.setForeground(Color.WHITE);
        field.setCaretColor(cyan);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(67, 82, 128)),
                new EmptyBorder(10, 12, 10, 12)));
        return field;
    }

    private void addField(JPanel panel, int row, String label,
                          JTextField field) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 4, 8, 12);
        g.gridy = row;
        g.gridx = 0;
        g.anchor = GridBagConstraints.WEST;

        JLabel l = new JLabel(label);
        l.setForeground(muted);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        panel.add(l, g);

        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 4, 8, 4);
        panel.add(field, g);
    }

    private JPanel metricCard(String title, Color accent) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(16, 23, 45));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(62, 76, 115)),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel name = new JLabel(title);
        name.setForeground(accent);
        name.setFont(new Font("SansSerif", Font.BOLD, 11));

        JLabel value = new JLabel("—");
        value.setForeground(Color.WHITE);
        value.setFont(new Font("Monospaced", Font.BOLD, 19));

        JLabel time = new JLabel("Execution: —");
        time.setForeground(muted);
        time.setFont(new Font("SansSerif", Font.PLAIN, 11));

        p.add(name);
        p.add(Box.createVerticalStrut(12));
        p.add(value);
        p.add(Box.createVerticalStrut(9));
        p.add(time);

        // Store the labels so they can be updated after searching.
        p.putClientProperty("valueLabel", value);
        p.putClientProperty("timeLabel", time);
        return p;
    }

    private JLabel metricValue(JPanel card) {
        return (JLabel) card.getClientProperty("valueLabel");
    }

    private JLabel metricTime(JPanel card) {
        return (JLabel) card.getClientProperty("timeLabel");
    }

    private static List<Integer> naiveSearch(
            String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return positions;
        }

        for (int i = 0; i <= n - m; i++) {
            int j = 0;

            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }

            if (j == m) {
                positions.add(i);
            }
        }

        return positions;
    }

    private static List<Integer> rabinKarp(
            String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return positions;
        }

        int base = 256;
        int prime = 101;

        long patternHash = 0;
        long windowHash = 0;
        long highest = 1;

        for (int i = 0; i < m - 1; i++) {
            highest = (highest * base) % prime;
        }

        for (int i = 0; i < m; i++) {
            patternHash =
                    (base * patternHash + pattern.charAt(i)) % prime;
            windowHash =
                    (base * windowHash + text.charAt(i)) % prime;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == windowHash
                    && text.regionMatches(i, pattern, 0, m)) {
                positions.add(i);
            }

            if (i < n - m) {
                windowHash = (base * (windowHash
                        - text.charAt(i) * highest)
                        + text.charAt(i + m)) % prime;

                if (windowHash < 0) {
                    windowHash += prime;
                }
            }
        }

        return positions;
    }

    private void showPreview(String text, String pattern,
                             List<Integer> positions) {
        StyledDocument doc = previewPane.getStyledDocument();

        try {
            doc.remove(0, doc.getLength());

            SimpleAttributeSet normal = new SimpleAttributeSet();
            StyleConstants.setForeground(
                    normal, new Color(225, 235, 255));
            StyleConstants.setFontFamily(normal, "Monospaced");
            StyleConstants.setFontSize(normal, 15);

            doc.insertString(0, text, normal);

            SimpleAttributeSet highlight = new SimpleAttributeSet();
            StyleConstants.setBackground(
                    highlight, new Color(48, 190, 125));
            StyleConstants.setForeground(highlight, Color.BLACK);
            StyleConstants.setBold(highlight, true);

            for (int pos : positions) {
                doc.setCharacterAttributes(
                        pos, pattern.length(), highlight, false);
            }
        } catch (BadLocationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // Animated gradient banner
    private static class GradientHeader extends JPanel {
        private float phase = 0;

        GradientHeader() {
            setOpaque(false);
        }

        void setPhase(float phase) {
            this.phase = phase;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g.setPaint(new GradientPaint(
                    0, 0, new Color(36, 48, 112),
                    w, h, new Color(91, 42, 155)));
            g.fillRoundRect(0, 0, w, h, 22, 22);

            int glowX = (int) ((w + 180) * phase) - 90;
            g.setColor(new Color(70, 210, 255, 28));
            g.fillOval(glowX - 100, -70, 200, h + 140);

            g.setColor(new Color(255, 255, 255, 35));
            g.drawRoundRect(0, 0, w - 1, h - 1, 22, 22);

            g.dispose();
        }
    }

    // Rounded gradient buttons with hover effect
    private static class RoundedButton extends JButton {
        private final Color start;
        private final Color end;
        private boolean hovered = false;

        RoundedButton(String text, Color start, Color end) {
            super(text);
            this.start = start;
            this.end = end;

            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setBorder(new EmptyBorder(12, 18, 12, 18));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            Color a = hovered ? start.brighter() : start;
            Color b = hovered ? end.brighter() : end;

            g.setPaint(new GradientPaint(0, 0, a, w, h, b));
            g.fillRoundRect(0, 0, w, h, 16, 16);
            g.dispose();

            super.paintComponent(graphics);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // Use the default Java look and feel.
        }

        SwingUtilities.invokeLater(() -> {
            StringMatching app = new StringMatching();
            app.setVisible(true);
        });
    }
}
