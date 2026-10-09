import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioSystem;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class PenaltyShootout extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int MAX_LEVEL = 10000;
    private static final Color NAVY = new Color(16, 29, 49);
    private static final Color GREEN = new Color(28, 117, 74);
    private static final Color[] DIFFICULTY_COLORS = {
            new Color(39, 117, 214), new Color(44, 151, 83), new Color(226, 190, 45),
            new Color(235, 139, 42), new Color(205, 54, 60)
    };
    private static final double[] DIFFICULTY_SAVE_RATES = {.01, .18, .52, .82, .97};
    private static final String[] ZONES = {"LEFT", "CENTER", "RIGHT"};
    private static final String CUSTOM_PLAYER = "Create your own";
    private static final String[] PLAYER_OPTIONS = {
            "Lionel Messi", "Cristiano Ronaldo", "Kylian Mbappe", "Harry Kane", "Erling Haaland",
            "Neymar", "Robert Lewandowski", "Mohamed Salah", "Bruno Fernandes", "Kevin De Bruyne",
            "Jorginho", "James Ward-Prowse", "Ivan Toney", "Cole Palmer", "Son Heung-min",
            "Karim Benzema", "Antoine Griezmann", "Luka Modric", "Bukayo Saka", "Olivier Giroud",
            "Edinson Cavani", "Sergio Ramos", "Ciro Immobile", "Lautaro Martinez", "Vinicius Junior"
    };
    private static final PlayerKit[] PLAYER_KITS = createUnverifiedPlayerKits();
    private static final Map<String, PenaltyRecord> PENALTY_RECORDS = Map.ofEntries(
            Map.entry("Lionel Messi", new PenaltyRecord(117, 35,
                    "https://www.transfermarkt.com/lionel-messi/elfmetertore/spieler/28003")),
            Map.entry("Cristiano Ronaldo", new PenaltyRecord(183, 36,
                    "https://www.transfermarkt.com/cristiano-ronaldo/elfmetertore/spieler/8198")),
            Map.entry("Kylian Mbappe", new PenaltyRecord(66, 16,
                    "https://www.transfermarkt.com/kylian-mbappe/elfmetertore/spieler/342229")),
            Map.entry("Harry Kane", new PenaltyRecord(111, 15,
                    "https://www.transfermarkt.com/harry-kane/elfmetertore/spieler/132098")),
            Map.entry("Erling Haaland", new PenaltyRecord(57, 10,
                    "https://www.transfermarkt.com/erling-haaland/elfmetertore/spieler/418560")),
            Map.entry("Neymar", new PenaltyRecord(97, 22,
                    "https://www.transfermarkt.com/neymar/elfmetertore/spieler/68290")),
            Map.entry("Robert Lewandowski", new PenaltyRecord(91, 12,
                    "https://www.transfermarkt.com/robert-lewandowski/elfmetertore/spieler/38253")),
            Map.entry("Mohamed Salah", new PenaltyRecord(56, 13,
                    "https://www.transfermarkt.com/mohamed-salah/elfmetertore/spieler/148455")),
            Map.entry("Bruno Fernandes", new PenaltyRecord(68, 8,
                    "https://www.transfermarkt.com/bruno-fernandes/elfmetertore/spieler/240306")),
            Map.entry("Kevin De Bruyne", new PenaltyRecord(15, 2,
                    "https://www.transfermarkt.com/kevin-de-bruyne/elfmetertore/spieler/88755")),
            Map.entry("Jorginho", new PenaltyRecord(51, 8,
                    "https://www.transfermarkt.com/jorginho/elfmetertore/spieler/102017")),
            Map.entry("James Ward-Prowse", new PenaltyRecord(20, 6,
                    "https://www.transfermarkt.com/james-ward-prowse/elfmetertore/spieler/181579")),
            Map.entry("Ivan Toney", new PenaltyRecord(62, 5,
                    "https://www.transfermarkt.com/ivan-toney/elfmetertore/spieler/251664")),
            Map.entry("Cole Palmer", new PenaltyRecord(24, 2,
                    "https://www.transfermarkt.com/cole-palmer/elfmetertore/spieler/568177")),
            Map.entry("Son Heung-min", new PenaltyRecord(18, 6,
                    "https://www.transfermarkt.com/heung-min-son/elfmetertore/spieler/91845")),
            Map.entry("Karim Benzema", new PenaltyRecord(45, 14,
                    "https://www.transfermarkt.com/karim-benzema/elfmetertore/spieler/18922")),
            Map.entry("Antoine Griezmann", new PenaltyRecord(25, 14,
                    "https://www.transfermarkt.com/antoine-griezmann/elfmetertore/spieler/125781")),
            Map.entry("Luka Modric", new PenaltyRecord(24, 6,
                    "https://www.transfermarkt.com/luka-modric/elfmetertore/spieler/27992")),
            Map.entry("Bukayo Saka", new PenaltyRecord(18, 3,
                    "https://www.transfermarkt.com/bukayo-saka/elfmetertore/spieler/433177")),
            Map.entry("Olivier Giroud", new PenaltyRecord(37, 5,
                    "https://www.transfermarkt.com/olivier-giroud/elfmetertore/spieler/82442")),
            Map.entry("Edinson Cavani", new PenaltyRecord(63, 16,
                    "https://www.transfermarkt.com/edinson-cavani/elfmetertore/spieler/48280")),
            Map.entry("Sergio Ramos", new PenaltyRecord(37, 6,
                    "https://www.transfermarkt.com/sergio-ramos/elfmetertore/spieler/25557")),
            Map.entry("Ciro Immobile", new PenaltyRecord(86, 19,
                    "https://www.transfermarkt.com/ciro-immobile/elfmetertore/spieler/105521")),
            Map.entry("Lautaro Martinez", new PenaltyRecord(19, 10,
                    "https://www.transfermarkt.com/lautaro-martinez/elfmetertore/spieler/406625")),
            Map.entry("Vinicius Junior", new PenaltyRecord(14, 6,
                    "https://www.transfermarkt.com/vinicius-junior/elfmetertore/spieler/371998")));
    private static final String[] KEEPER_OPTIONS = {
            "Unai Simon", "Emiliano Martinez", "Mike Maignan", "Jordan Pickford", "Diogo Costa",
            "Alisson Becker", "Yassine Bounou", "Thibaut Courtois", "Bart Verbruggen",
            "Luis Angel Malagon", "Camilo Vargas", "Gregor Kobel", "Oliver Baumann",
            "Dominik Livakovic", "Gianluigi Donnarumma", "Matt Freese", "Zion Suzuki",
            "Edouard Mendy", "Orjan Nyland", "Sergio Rochet", "Mads Hermansen",
            "Patrick Pentz", "Alireza Beiranvand", "Hernan Galindez", "Mohamed El Shenawy"
    };
    private static final String[] KEEPER_NATIONS = {
            "Spain", "Argentina", "France", "England", "Portugal",
            "Brazil", "Morocco", "Belgium", "Netherlands", "Mexico",
            "Colombia", "Switzerland", "Germany", "Croatia", "Italy",
            "USA", "Japan", "Senegal", "Norway", "Uruguay",
            "Denmark", "Austria", "IR Iran", "Ecuador", "Egypt"
    };
    private static final int[] KEEPER_NUMBERS = {
            23, 23, 16, 1, 22, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1
    };
    private static final Color[] KEEPER_KIT_COLORS = {
            new Color(227, 35, 47), new Color(28, 142, 81), new Color(28, 76, 160),
            new Color(229, 211, 51), new Color(28, 64, 135), new Color(34, 102, 184),
            new Color(180, 27, 49), new Color(93, 44, 146), new Color(23, 69, 123),
            new Color(37, 98, 168), new Color(239, 205, 38), new Color(230, 218, 46),
            new Color(34, 100, 80), new Color(27, 56, 113), new Color(52, 90, 193),
            new Color(42, 140, 91), new Color(203, 42, 52), new Color(27, 109, 78),
            new Color(48, 96, 159), new Color(45, 139, 99), new Color(204, 43, 53),
            new Color(39, 103, 66), new Color(42, 122, 93), new Color(37, 99, 164),
            new Color(212, 176, 38)
    };
    private static PlayerKit[] createUnverifiedPlayerKits() {
        PlayerKit[] kits = new PlayerKit[PLAYER_OPTIONS.length];
        for (int i = 0; i < kits.length; i++) {
            kits[i] = new PlayerKit("Not verified", 0, "PS",
                    new Color(70, 103, 120), new Color(164, 190, 190), 0, false);
        }
        return kits;
    }

    private static final String KEEPER_RANKING_SOURCE =
            "https://inside.fifa.com/fifa-rankings/world-ranking/men";
    private static final String KEEPER_RANKING_DATE = "7 October 2026";
    private static final String[] CLUBS = {
            "Bayern Munich", "Real Madrid", "Paris Saint-Germain", "Liverpool", "Inter Milan",
            "Manchester City", "Arsenal", "Barcelona", "Bayer Leverkusen", "Atletico Madrid",
            "Borussia Dortmund", "Chelsea", "Roma", "Benfica", "Sporting CP", "Atalanta",
            "Aston Villa", "Eintracht Frankfurt", "Tottenham Hotspur", "Porto", "Manchester United",
            "Fiorentina", "Club Brugge", "Real Betis", "Juventus"
    };
    private static final int[] UEFA_RANKS = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 15, 17, 17, 19, 20, 21, 22, 23, 24, 25
    };
    private static final String[] STADIUMS = {
            "Allianz Arena", "Santiago Bernabeu", "Parc des Princes", "Anfield", "San Siro",
            "Etihad Stadium", "Emirates Stadium", "Spotify Camp Nou", "BayArena",
            "Riyadh Air Metropolitano", "Signal Iduna Park", "Stamford Bridge", "Stadio Olimpico",
            "Estadio da Luz", "Estadio Jose Alvalade", "New Balance Arena", "Villa Park",
            "Deutsche Bank Park", "Tottenham Hotspur Stadium", "Estadio do Dragao", "Old Trafford",
            "Stadio Artemio Franchi", "Jan Breydel Stadium", "Estadio de La Cartuja", "Allianz Stadium"
    };
    private static final Color[] CLUB_COLORS = {
            new Color(220, 27, 48), new Color(255, 255, 255), new Color(27, 39, 97),
            new Color(200, 16, 46), new Color(16, 49, 120), new Color(108, 188, 231),
            new Color(220, 26, 45), new Color(24, 45, 105), new Color(210, 25, 45),
            new Color(204, 26, 49), new Color(255, 217, 0), new Color(3, 70, 148),
            new Color(139, 35, 54), new Color(214, 22, 40), new Color(0, 106, 71),
            new Color(19, 46, 101), new Color(111, 40, 59), new Color(225, 26, 47),
            new Color(19, 32, 67), new Color(0, 58, 122), new Color(218, 28, 46),
            new Color(105, 47, 143), new Color(30, 63, 135), new Color(0, 133, 73),
            new Color(18, 18, 18)
    };
    private static final Color[] CLUB_SECONDARY_COLORS = {
            Color.WHITE, new Color(227, 190, 111), new Color(218, 218, 225),
            Color.WHITE, new Color(205, 25, 45), Color.WHITE, Color.WHITE,
            new Color(167, 29, 59), Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE,
            new Color(225, 154, 46), Color.WHITE, Color.WHITE, Color.BLACK,
            new Color(116, 188, 219), Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE,
            Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE
    };
    private static final Preferences PREFS = Preferences.userNodeForPackage(PenaltyShootout.class);
    private final Random random = new Random();
    private final CardLayout cards = new CardLayout();
    private final JPanel screens = new JPanel(cards);
    private final JLabel homeProgress = new JLabel("", SwingConstants.CENTER);
    private final JLabel difficultyPreview = new JLabel("", SwingConstants.CENTER);
    private final JButton playButton = new JButton();
    private final JButton tournamentButton = new JButton("PENALTY CUP");
    private final JComboBox<String> difficultySelector = new JComboBox<>(
            new String[]{"Easy", "Normal", "Difficult", "Superstar", "Legend"});
    private final JComboBox<String> stadiumSelector = new JComboBox<>(CLUBS);
    private final JComboBox<String> playerSelector = new JComboBox<>(playerOptions());
    private final JLabel stadiumInfo = new JLabel("", SwingConstants.CENTER);
    private final JLabel playerInfo = new JLabel("", SwingConstants.CENTER);
    private final JLabel playerPortrait = new JLabel();
    private final JLabel playerDataStatus = new JLabel("", SwingConstants.CENTER);
    private final JLabel[] keeperStatLabels = new JLabel[KEEPER_OPTIONS.length];
    private final JLabel settingsInfo = new JLabel("", SwingConstants.CENTER);
    private final JComboBox<String> graphicsSelector = new JComboBox<>(
            new String[]{"Low", "Medium", "High"});
    private final JSlider gameVolumeSlider = new JSlider(0, 100, 75);
    private final JSlider stadiumVolumeSlider = new JSlider(0, 100, 45);
    private Timer volumePreviewTimer;
    private boolean previewStadiumVolume;
    private transient SoundManager soundManager = new SoundManager();
    private boolean suppressPlayerSelectionAction;
    private transient SwingWorker<PlayerKitUpdate, Void> playerKitWorker;
    private boolean queuedPlayerKitRefresh;
    private JTabbedPane homeTabs;
    private JPanel settingsPanel;
    private final JLabel tournamentResultTitle = styledLabel("", 28, Color.WHITE);
    private final JLabel tournamentResultScore = styledLabel("", 38, new Color(255, 221, 133));
    private final JLabel tournamentResultDetails = styledLabel("", 15, new Color(220, 237, 244));
    private final JPanel tournamentResultsPanel = new JPanel(new BorderLayout(12, 12));
    private Timer tournamentReplyTimer;
    private boolean tournamentMode;
    private int tournamentRound;
    private int tournamentPlayerGoals;
    private int tournamentOpponentGoals;
    private int tournamentPlayerKicks;
    private int tournamentOpponentKicks;
    private boolean tournamentSuddenDeath;
    private final RoadmapPanel levelGrid = new RoadmapPanel();
    private JScrollPane roadmapScroll;
    private final JLabel levelTitle = new JLabel("", SwingConstants.CENTER);
    private final JLabel challengeLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel progressLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private final FieldPanel field = new FieldPanel();
    private final JButton startButton = new JButton("START CHALLENGE");
    private final JButton retryButton = new JButton("TRY AGAIN");
    private final JButton nextLevelButton = new JButton("NEXT LEVEL");

    private int highestCompletedLevel = Math.max(0, Math.min(MAX_LEVEL - 1,
            PREFS.getInt("highestCompletedLevel", PREFS.getInt("unlockedLevel", 1) - 1)));
    private int unlockedLevel = highestCompletedLevel + 1;
    private int selectedLevel = unlockedLevel;
    private int goals;
    private int shots;
    private int streak;
    private int sequenceIndex;
    private int timeLeft;
    private transient Challenge challenge;
    private boolean active;
    private boolean shotInProgress;
    private boolean keeperCaughtBall;
    private boolean keeperHasSecuredBall;
    private double aimX = .5;
    private double aimY = .38;
    private Timer clockTimer;
    private Timer shotTimer;
    private Timer sceneTimer;
    private long shotStartedAt;
    private int keeperZone = 1;
    private boolean keeperDiving;
    private double keeperX = .5;
    private double keeperStartX = .5;
    private double keeperTargetX = .5;
    private double ballX = .5;
    private double ballY = .9;
    private double ballStartX = .5;
    private double ballStartY = .9;
    private double ballTargetX = .5;
    private double ballTargetY = .38;
    private int lastShotResult;
    private int diveFrame;
    private int diveDirection = 1;
    private long keeperDiveStartedAt;
    private long nextPatrolAt;
    private String customPlayerName = PREFS.get("customPlayerName", "My Player");
    private int customPlayerNumber = Math.max(1, Math.min(99, PREFS.getInt("customPlayerNumber", 10)));
    private Color customPlayerColor = new Color(PREFS.getInt("customPlayerColor", new Color(45, 129, 205).getRGB()), true);

    public PenaltyShootout() {
        super("Penalty Shootout");
        loadCachedPlayerKits();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 760));
        setSize(940, 820);
        setLocationRelativeTo(null);
        screens.add(createHomeScreen(), "home");
        screens.add(createGameScreen(), "game");
        screens.add(createTournamentResultsScreen(), "tournament-results");
        add(screens);
        refreshLevelBrowser();
        cards.show(screens, "home");
        soundManager.startHomeMusic();
        sceneTimer = new Timer(35, event -> animateScene());
        sceneTimer.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                sceneTimer.stop();
                if (clockTimer != null) clockTimer.stop();
                if (shotTimer != null) shotTimer.stop();
                if (tournamentReplyTimer != null) tournamentReplyTimer.stop();
                if (volumePreviewTimer != null) volumePreviewTimer.stop();
                soundManager.stopCrowd();
                soundManager.stopHomeMusic();
            }
        });
        if (highestCompletedLevel == 0 && unlockedLevel == 1
                && !PREFS.getBoolean("tutorialCompleted", false)) {
            SwingUtilities.invokeLater(this::showFirstRunTutorial);
        }
        SwingUtilities.invokeLater(() -> refreshPlayerKit(false));
    }

    private void showFirstRunTutorial() {
        if (!isDisplayable() || PREFS.getBoolean("tutorialCompleted", false)) return;
        TutorialDialog tutorial = new TutorialDialog();
        tutorial.setVisible(true);
    }

    private JTabbedPane createHomeScreen() {
        JPanel root = new JPanel(new BorderLayout(14, 12));
        root.setBackground(NAVY);
        root.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));

        JPanel hero = new HomeHeroPanel();
        hero.setLayout(new javax.swing.BoxLayout(hero, javax.swing.BoxLayout.Y_AXIS));
        hero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(109, 211, 171, 125), 1),
                BorderFactory.createEmptyBorder(11, 20, 10, 20)));
        JLabel kicker = styledLabel("MATCHDAY  •  THE MOMENT IS YOURS", 12, new Color(159, 240, 204));
        JLabel title = styledLabel("PENALTY SHOOTOUT", 31, Color.WHITE);
        JLabel intro = styledLabel("10,000 challenges. One keeper. Make every shot count.",
                14, new Color(220, 237, 244));
        int savedDifficulty = Math.max(0, Math.min(4, PREFS.getInt("difficulty", 0)));
        difficultySelector.setSelectedIndex(savedDifficulty);
        JPanel difficultyPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 4));
        difficultyPanel.setBackground(new Color(8, 22, 34, 185));
        difficultyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 35)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        JLabel difficultyHeading = styledLabel("MATCH LEVEL", 12, new Color(213, 229, 235));
        difficultyPanel.add(difficultyHeading);
        difficultySelector.setFont(new Font("SansSerif", Font.BOLD, 13));
        difficultySelector.setPreferredSize(new Dimension(145, 28));
        difficultySelector.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel item = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                int difficulty = index < 0 ? difficultySelector.getSelectedIndex() : index;
                item.setOpaque(true);
                item.setBackground(DIFFICULTY_COLORS[difficulty]);
                item.setForeground(Color.WHITE);
                return item;
            }
        });
        updateDifficultyColor();
        difficultySelector.addActionListener(event -> {
            PREFS.putInt("difficulty", difficultySelector.getSelectedIndex());
            updateDifficultyColor();
            updateDifficultyPreview();
            if (challenge != null) {
                challenge = applyDifficulty(makeChallenge(selectedLevel), difficultySelector.getSelectedIndex());
                timeLeft = challenge.seconds;
                challengeLabel.setText(describeChallenge() + "  •  " + difficultySummary());
                updateProgress();
            }
        });
        difficultyPanel.add(difficultySelector);
        difficultyPanel.setPreferredSize(new Dimension(285, 38));
        difficultyPanel.setMaximumSize(new Dimension(285, 38));
        homeProgress.setForeground(new Color(191, 210, 226));
        homeProgress.setFont(new Font("SansSerif", Font.BOLD, 12));
        for (JLabel label : new JLabel[]{kicker, title, intro, homeProgress}) {
            label.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        }
        difficultyPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        hero.add(kicker);
        hero.add(javax.swing.Box.createVerticalStrut(1));
        hero.add(title);
        hero.add(javax.swing.Box.createVerticalStrut(1));
        hero.add(intro);
        hero.add(javax.swing.Box.createVerticalStrut(6));
        hero.add(difficultyPanel);
        hero.add(javax.swing.Box.createVerticalStrut(6));
        JPanel modeButtons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 12, 0));
        modeButtons.setOpaque(false);
        playButton.setFont(new Font("SansSerif", Font.BOLD, 17));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(new Color(29, 169, 111));
        playButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 255, 222, 150), 1),
                BorderFactory.createEmptyBorder(4, 18, 4, 18)));
        playButton.setFocusPainted(false);
        playButton.setPreferredSize(new Dimension(260, 44));
        playButton.setMaximumSize(new Dimension(260, 44));
        playButton.addActionListener(event -> openLevel(unlockedLevel));
        tournamentButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        tournamentButton.setForeground(Color.WHITE);
        tournamentButton.setBackground(new Color(40, 61, 82));
        tournamentButton.setFocusPainted(false);
        tournamentButton.setPreferredSize(new Dimension(185, 44));
        tournamentButton.setToolTipText("Play a five-round shootout with sudden death if tied.");
        tournamentButton.addActionListener(event -> startTournament());
        modeButtons.add(playButton);
        modeButtons.add(tournamentButton);
        modeButtons.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        hero.add(modeButtons);
        hero.add(javax.swing.Box.createVerticalStrut(3));
        difficultyPreview.setForeground(new Color(255, 221, 133));
        difficultyPreview.setFont(new Font("SansSerif", Font.BOLD, 11));
        difficultyPreview.setToolTipText("Difficulty changes challenge requirements and goalkeeper ability.");
        difficultyPreview.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        hero.add(difficultyPreview);
        root.add(hero, BorderLayout.NORTH);

        JPanel browser = new JPanel(new BorderLayout(10, 9));
        browser.setOpaque(false);
        JPanel browserHeading = new JPanel(new BorderLayout(8, 2));
        browserHeading.setOpaque(false);
        JLabel heading = styledLabel("CAMPAIGN", 18, Color.WHITE);
        heading.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel levelHint = styledLabel("Complete a level to unlock the next", 12, new Color(159, 180, 194));
        levelHint.setHorizontalAlignment(SwingConstants.CENTER);
        JButton settingsButton = new JButton();
        settingsButton.setIcon(new GearIcon());
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setBackground(new Color(40, 61, 82));
        settingsButton.setFocusPainted(false);
        settingsButton.setToolTipText("Settings");
        settingsButton.getAccessibleContext().setAccessibleName("Settings");
        settingsButton.setPreferredSize(new Dimension(42, 34));
        settingsButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        settingsButton.addActionListener(event -> openSettingsDialog());
        browserHeading.add(heading, BorderLayout.WEST);
        browserHeading.add(levelHint, BorderLayout.CENTER);
        browserHeading.add(settingsButton, BorderLayout.EAST);
        browser.add(browserHeading, BorderLayout.NORTH);
        levelGrid.setOpaque(false);
        roadmapScroll = new JScrollPane(levelGrid);
        roadmapScroll.setBorder(BorderFactory.createEmptyBorder());
        roadmapScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        roadmapScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        roadmapScroll.getVerticalScrollBar().setUnitIncrement(24);
        roadmapScroll.getHorizontalScrollBar().setUnitIncrement(24);
        roadmapScroll.getViewport().setOpaque(false);
        roadmapScroll.setOpaque(false);
        browser.add(roadmapScroll, BorderLayout.CENTER);

        JLabel scrollHint = styledLabel("FOLLOW THE PATH DOWN  •  SCROLL TO EXPLORE ALL 10,000 LEVELS",
                11, new Color(159, 205, 183));
        scrollHint.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        browser.add(scrollHint, BorderLayout.SOUTH);
        root.add(browser, BorderLayout.CENTER);

        homeTabs = new JTabbedPane(SwingConstants.TOP);
        homeTabs.addTab("CAMPAIGN", root);
        homeTabs.addTab("STADIUM", createStadiumTab());
        homeTabs.addTab("PENALTY TAKER", createPlayerTab());
        homeTabs.addTab("KEEPERS", createKeeperTab());
        settingsPanel = createSettingsTab();
        homeTabs.setBackground(NAVY);
        homeTabs.setForeground(Color.WHITE);
        return homeTabs;
    }

    private JPanel createStadiumTab() {
        JPanel panel = createSelectionTab("CHOOSE YOUR STADIUM",
                "Clubs follow the 2026 UEFA five-year coefficient ranking (tied places included). "
                + "Stadiums are stylized previews, not official images.");
        int savedStadium = Math.max(0, Math.min(CLUBS.length - 1, PREFS.getInt("stadium", 0)));
        stadiumSelector.setSelectedIndex(savedStadium);
        stadiumSelector.setFont(new Font("SansSerif", Font.BOLD, 16));
        stadiumSelector.addActionListener(event -> {
            int index = stadiumSelector.getSelectedIndex();
            PREFS.putInt("stadium", index);
            updateStadiumInfo();
            field.repaint();
        });
        stadiumInfo.setForeground(new Color(222, 237, 242));
        stadiumInfo.setFont(new Font("SansSerif", Font.BOLD, 18));
        JPanel options = new JPanel(new BorderLayout(10, 14));
        options.setOpaque(false);
        options.add(stadiumSelector, BorderLayout.NORTH);
        options.add(new StadiumPreviewPanel(), BorderLayout.CENTER);
        options.add(stadiumInfo, BorderLayout.SOUTH);
        panel.add(options, BorderLayout.CENTER);
        updateStadiumInfo();
        return panel;
    }

    private JPanel createKeeperTab() {
        JPanel panel = createSelectionTab("KEEPER LINEUP",
                "Levels follow the official FIFA men's national-team ranking (#25 to #1), then repeat every 25 levels. "
                + "Each country is represented by a male goalkeeper; FIFA does not rank individual keepers. "
                + "Save records accumulate from on-target shots in your games.");
        JPanel lineup = new JPanel(new GridLayout(0, 2, 10, 10));
        lineup.setOpaque(false);
        for (int i = 0; i < KEEPER_OPTIONS.length; i++) {
            JPanel keeperCard = new JPanel(new BorderLayout(8, 4));
            keeperCard.setOpaque(false);
            keeperCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(91, 124, 141, 105)),
                    BorderFactory.createEmptyBorder(5, 7, 5, 7)));
            JLabel portrait = new JLabel(new PlayerPortraitIcon(KEEPER_OPTIONS[i],
                    KEEPER_NUMBERS[i], KEEPER_KIT_COLORS[i], Color.WHITE,
                    initials(KEEPER_NATIONS[i]), 0, true, 62, 78));
            keeperCard.add(portrait, BorderLayout.WEST);
            JPanel keeperDetails = new JPanel(new GridLayout(0, 1, 2, 2));
            keeperDetails.setOpaque(false);
            keeperDetails.add(styledLabel("<html><b>FIFA #" + (i + 1) + " · " + KEEPER_NATIONS[i]
                    + "</b><br>" + KEEPER_OPTIONS[i] + "<br><span style='color:#9FBCBF'>"
                    + KEEPER_NATIONS[i] + " keeper · #" + KEEPER_NUMBERS[i]
                    + "</span></html>", 12, new Color(220, 237, 244)));
            keeperStatLabels[i] = styledLabel("", 11, new Color(159, 205, 183));
            updateKeeperStatLabel(i);
            keeperDetails.add(keeperStatLabels[i]);
            keeperCard.add(keeperDetails, BorderLayout.CENTER);
            lineup.add(keeperCard);
        }
        JScrollPane scroll = new JScrollPane(lineup);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);
        JLabel source = styledLabel(
                "<html><div style='text-align:center'>Source: FIFA/Coca-Cola Men's World Ranking, "
                + KEEPER_RANKING_DATE + ". The rank is for national teams, not goalkeepers.<br>"
                + KEEPER_RANKING_SOURCE + "<br>Player portraits and kit marks are stylized, not official photos or crests."
                + "</div></html>",
                11, new Color(174, 201, 211));
        source.setToolTipText(KEEPER_RANKING_SOURCE);
        panel.add(source, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createPlayerTab() {
        JPanel panel = createSelectionTab("CHOOSE YOUR PENALTY TAKER",
                "Club and shirt number are checked against ESPN's current-season team roster.");
        String savedPlayer = PREFS.get("player", PLAYER_OPTIONS[0]);
        if (savedPlayer.equals(CUSTOM_PLAYER)) savedPlayer = CUSTOM_PLAYER;
        playerSelector.setSelectedItem(savedPlayer);
        playerSelector.setFont(new Font("SansSerif", Font.BOLD, 16));
        playerSelector.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel item = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                if (CUSTOM_PLAYER.equals(value)) {
                    item.setIcon(new PlayerPortraitIcon(customPlayerName, customPlayerNumber,
                            customPlayerColor, Color.WHITE, "MY", 0, false, 30, 38));
                } else {
                    int playerIndex = Arrays.asList(PLAYER_OPTIONS).indexOf(String.valueOf(value));
                    if (playerIndex >= 0) {
                        PlayerKit kit = PLAYER_KITS[playerIndex];
                        item.setIcon(new PlayerPortraitIcon(String.valueOf(value), kit.number,
                                kit.primary, kit.secondary, kit.monogram, kit.pattern, false, 30, 38));
                    }
                }
                item.setIconTextGap(8);
                return item;
            }
        });
        playerSelector.addActionListener(event -> {
            if (suppressPlayerSelectionAction) return;
            Object selected = playerSelector.getSelectedItem();
            if (CUSTOM_PLAYER.equals(selected)) {
                if (!editCustomPlayer()) {
                    String previousPlayer = PREFS.get("player", PLAYER_OPTIONS[0]);
                    playerSelector.setSelectedItem(previousPlayer);
                } else {
                    PREFS.put("player", CUSTOM_PLAYER);
                    updatePlayerInfo();
                    field.repaint();
                }
                return;
            }
            PREFS.put("player", String.valueOf(selected));
            updatePlayerInfo();
            field.repaint();
            refreshPlayerKit(false);
        });
        playerInfo.setForeground(new Color(222, 237, 242));
        playerInfo.setFont(new Font("SansSerif", Font.BOLD, 17));
        JButton createPlayerButton = new JButton("CREATE / EDIT MY PLAYER");
        createPlayerButton.setForeground(Color.WHITE);
        createPlayerButton.setBackground(new Color(29, 113, 88));
        createPlayerButton.setFocusPainted(false);
        createPlayerButton.addActionListener(event -> {
            if (editCustomPlayer()) {
                suppressPlayerSelectionAction = true;
                playerSelector.setSelectedItem(CUSTOM_PLAYER);
                suppressPlayerSelectionAction = false;
                PREFS.put("player", CUSTOM_PLAYER);
                updatePlayerInfo();
                field.repaint();
            }
        });
        JPanel options = new JPanel(new BorderLayout(10, 14));
        options.setOpaque(false);
        options.add(playerSelector, BorderLayout.NORTH);
        JPanel details = new JPanel(new BorderLayout(8, 14));
        details.setOpaque(false);
        playerPortrait.setHorizontalAlignment(SwingConstants.CENTER);
        playerPortrait.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));
        details.add(playerPortrait, BorderLayout.NORTH);
        details.add(playerInfo, BorderLayout.CENTER);
        JButton refreshPlayerDataButton = new JButton("REFRESH CLUB & NUMBER");
        refreshPlayerDataButton.addActionListener(event -> refreshPlayerKit(true));
        JPanel playerFooter = new JPanel(new GridLayout(0, 1, 5, 5));
        playerFooter.setOpaque(false);
        playerFooter.add(createPlayerButton);
        playerDataStatus.setForeground(new Color(174, 201, 211));
        playerDataStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        playerFooter.add(playerDataStatus);
        playerFooter.add(refreshPlayerDataButton);
        details.add(playerFooter, BorderLayout.SOUTH);
        options.add(details, BorderLayout.CENTER);
        panel.add(options, BorderLayout.CENTER);
        updatePlayerInfo();
        return panel;
    }

    private void openSettingsDialog() {
        JDialog dialog = new JDialog(this, "Game Settings", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(settingsPanel);
        dialog.setPreferredSize(new Dimension(540, 410));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createSettingsTab() {
        JPanel panel = createSelectionTab("GAME SETTINGS", "Adjust match audio and visual detail.");
        JPanel controls = new JPanel(new GridLayout(0, 1, 8, 8));
        controls.setOpaque(false);
        int master = Math.max(0, Math.min(100, PREFS.getInt("gameVolume", 75)));
        int crowd = Math.max(0, Math.min(100, PREFS.getInt("stadiumVolume", 45)));
        int graphics = Math.max(0, Math.min(2, PREFS.getInt("graphicsQuality", 2)));
        gameVolumeSlider.setValue(master);
        stadiumVolumeSlider.setValue(crowd);
        graphicsSelector.setSelectedIndex(graphics);
        controls.add(settingRow("GAME SOUND", gameVolumeSlider));
        controls.add(settingRow("STADIUM CROWD", stadiumVolumeSlider));
        controls.add(settingRow("GRAPHICS QUALITY", graphicsSelector));
        gameVolumeSlider.addChangeListener(event -> {
            PREFS.putInt("gameVolume", gameVolumeSlider.getValue());
            soundManager.setGameVolume(gameVolumeSlider.getValue());
            scheduleVolumePreview(false);
        });
        stadiumVolumeSlider.addChangeListener(event -> {
            PREFS.putInt("stadiumVolume", stadiumVolumeSlider.getValue());
            soundManager.setStadiumVolume(stadiumVolumeSlider.getValue());
            scheduleVolumePreview(true);
        });
        graphicsSelector.addActionListener(event -> {
            PREFS.putInt("graphicsQuality", graphicsSelector.getSelectedIndex());
            field.repaint();
            settingsInfo.setText("Graphics: " + graphicsSelector.getSelectedItem());
        });
        settingsInfo.setForeground(new Color(176, 202, 212));
        settingsInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        settingsInfo.setText("Move a volume slider to preview that sound at its current level.");
        JPanel content = new JPanel(new BorderLayout(10, 14));
        content.setOpaque(false);
        content.add(controls, BorderLayout.CENTER);
        content.add(settingsInfo, BorderLayout.SOUTH);
        panel.add(content, BorderLayout.CENTER);
        soundManager.setGameVolume(master);
        soundManager.setStadiumVolume(crowd);
        return panel;
    }

    private void scheduleVolumePreview(boolean previewCrowd) {
        previewStadiumVolume = previewCrowd;
        if (volumePreviewTimer == null) {
            volumePreviewTimer = new Timer(180, event -> {
                if (previewStadiumVolume) soundManager.previewCrowd();
                else soundManager.previewGame();
            });
            volumePreviewTimer.setRepeats(false);
        }
        volumePreviewTimer.restart();
    }

    private JPanel settingRow(String title, Component control) {
        JPanel row = new JPanel(new BorderLayout(12, 4));
        row.setOpaque(false);
        JLabel label = styledLabel(title, 13, Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(label, BorderLayout.WEST);
        row.add(control, BorderLayout.CENTER);
        return row;
    }

    private JPanel createSelectionTab(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout(18, 18));
        panel.setBackground(NAVY);
        panel.setBorder(BorderFactory.createEmptyBorder(36, 64, 36, 64));
        JLabel heading = styledLabel(title, 25, Color.WHITE);
        JLabel hint = styledLabel(description, 14, new Color(174, 201, 211));
        JPanel header = new JPanel(new GridLayout(0, 1, 8, 8));
        header.setOpaque(false);
        header.add(heading);
        header.add(hint);
        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    private void updateStadiumInfo() {
        int index = stadiumSelector.getSelectedIndex();
        stadiumInfo.setText("<html><center>UEFA 2026 rank #" + UEFA_RANKS[index] + " • " + CLUBS[index]
                + "<br><span style='color:#9FBCBF'>"
                + STADIUMS[index] + "</span></center></html>");
    }

    private void updatePlayerInfo() {
        boolean custom = CUSTOM_PLAYER.equals(playerSelector.getSelectedItem());
        String name = custom ? customPlayerName : String.valueOf(playerSelector.getSelectedItem());
        PlayerKit kit = selectedPlayerKit();
        String shirt;
        if (custom) {
            shirt = "Custom shirt · #" + customPlayerNumber;
        } else {
            List<String> details = new ArrayList<>();
            if (!"Not verified".equals(kit.team)) details.add("Club: " + kit.team);
            if (kit.number > 0) details.add("Shirt #: " + kit.number);
            shirt = details.isEmpty() ? "Club and shirt number not verified"
                    : String.join(" · ", details);
        }
        Color shirtColor = custom ? customPlayerColor : kit.primary;
        playerPortrait.setIcon(new PlayerPortraitIcon(name, custom ? customPlayerNumber : kit.number,
                shirtColor, custom ? Color.WHITE : kit.secondary,
                custom ? "MY" : kit.monogram, custom ? 0 : kit.pattern,
                false, 172, 190));
        PenaltyRecord penaltyRecord = custom ? null : PENALTY_RECORDS.get(name);
        if (!custom && penaltyRecord == null) {
            throw new IllegalStateException("Missing verified penalty record for " + name);
        }
        String penaltySkill = custom
                ? "Real-life penalty stats: not available for a custom character"
                : String.format("Real-life career penalties: %d scored / %d attempts · %.1f%% conversion",
                        penaltyRecord.scored, penaltyRecord.attempts(),
                        penaltyRecord.conversionRate() * 100);
        String recordSource = custom ? "Custom character · no real-life career record"
                : "Transfermarkt real-life career record only";
        playerInfo.setToolTipText(custom ? null : penaltyRecord.sourceUrl);
        String sourceNote = "Custom player";
        if (!custom) {
            int index = playerSelector.getSelectedIndex();
            long verifiedAt = PREFS.getLong("apiKit." + index + ".updated", 0L);
            if (index >= 0 && PREFS.getInt("apiKit." + index + ".sourceVersion", 0) == 5
                    && verifiedAt > 0) {
                sourceNote = "ESPN roster snapshot · checked "
                        + DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                                .format(new Date(verifiedAt));
            } else {
                sourceNote = "Club and number shown only after source verification";
            }
        }
        playerInfo.setText("<html><center><font size='+2'>" + name + "</font><br>"
                + "<span style='color:#9FBCBF'>" + shirt + "</span><br>"
                + "<span style='color:#9FBCBF'>" + penaltySkill + "</span><br>"
                + "<span style='color:#92AAB8'>" + recordSource + "</span><br>"
                + "<span style='color:#92AAB8'>Stylized illustration · " + sourceNote + "</span>"
                + "</center></html>");
    }

    private String keeperStatsKey(int keeperIndex) {
        return "matchStats.keeper." + keeperIndex;
    }

    private void updateKeeperStatLabel(int keeperIndex) {
        if (keeperIndex < 0 || keeperIndex >= keeperStatLabels.length
                || keeperStatLabels[keeperIndex] == null) return;
        String key = keeperStatsKey(keeperIndex);
        int saves = PREFS.getInt(key + ".saves", 0);
        int faced = PREFS.getInt(key + ".faced", 0);
        String text = faced == 0
                ? "Game penalties saved: 0 · rate appears after first on-target shot"
                : String.format("Game penalties saved: %d/%d (%.1f%%)",
                        saves, faced, saves * 100.0 / faced);
        keeperStatLabels[keeperIndex].setText(text);
    }

    private void recordShotStatistics(boolean keeperFaced, boolean keeperSaved) {
        int keeperIndex = keeperRankForLevel(selectedLevel) - 1;
        String keeperKey = keeperStatsKey(keeperIndex);
        int faced = PREFS.getInt(keeperKey + ".faced", 0) + (keeperFaced ? 1 : 0);
        int saves = PREFS.getInt(keeperKey + ".saves", 0) + (keeperSaved ? 1 : 0);
        PREFS.putInt(keeperKey + ".faced", faced);
        PREFS.putInt(keeperKey + ".saves", saves);
        updateKeeperStatLabel(keeperIndex);
    }

    private void loadCachedPlayerKits() {
        for (int index = 0; index < PLAYER_KITS.length; index++) {
            String prefix = "apiKit." + index + ".";
            if (PREFS.getInt(prefix + "sourceVersion", 0) != 5) continue;
            String team = PREFS.get(prefix + "team", "");
            int number = PREFS.getInt(prefix + "number", 0);
            if (!team.isEmpty() || number >= 1 && number <= 99) {
                PlayerKit original = PLAYER_KITS[index];
                String cachedTeam = team.isEmpty() ? "Not verified" : team;
                int cachedNumber = number >= 1 && number <= 99 ? number : 0;
                PLAYER_KITS[index] = new PlayerKit(cachedTeam, cachedNumber, original.monogram,
                        colorForClub(cachedTeam, original.primary), secondaryForClub(cachedTeam, original.secondary),
                        original.pattern, false);
            }
        }
    }

    private void refreshPlayerKit(boolean force) {
        int index = playerSelector.getSelectedIndex();
        if (index < 0 || index >= PLAYER_OPTIONS.length) {
            playerDataStatus.setText("Custom player data is managed in the player editor.");
            return;
        }
        long lastUpdated = PREFS.getLong("apiKit." + index + ".updated", 0L);
        long freshnessMillis = 6L * 60 * 60 * 1000;
        boolean hasVerifiedCache = PREFS.getInt("apiKit." + index + ".sourceVersion", 0) == 5;
        if (!force && hasVerifiedCache && System.currentTimeMillis() - lastUpdated < freshnessMillis) {
            playerDataStatus.setText("Cached ESPN roster data is less than 6 hours old.");
            return;
        }
        long now = System.currentTimeMillis();
        long lastRequest = PREFS.getLong("playerApi.lastRequestAt", 0L);
        long minimumRequestInterval = 2_000L;
        if (now - lastRequest < minimumRequestInterval) {
            long remainingSeconds = (minimumRequestInterval - (now - lastRequest) + 999) / 1000;
            playerDataStatus.setText("API cooldown: try again in " + remainingSeconds + " seconds.");
            return;
        }
        if (playerKitWorker != null && !playerKitWorker.isDone()) {
            queuedPlayerKitRefresh = true;
            playerDataStatus.setText("A live lookup is running. This player's lookup is queued.");
            return;
        }
        String playerName = PLAYER_OPTIONS[index];
        playerDataStatus.setText("Verifying club and shirt number for " + playerName + "…");
        PREFS.putLong("playerApi.lastRequestAt", now);
        playerKitWorker = new SwingWorker<PlayerKitUpdate, Void>() {
            @Override
            protected PlayerKitUpdate doInBackground() throws IOException, InterruptedException {
                return fetchPlayerKit(playerName);
            }

            @Override
            protected void done() {
                if (isCancelled()) return;
                try {
                    PlayerKitUpdate update = get();
                    if (update == null) {
                        playerDataStatus.setText("No exact match found on a current ESPN club roster; details remain unverified.");
                        return;
                    }
                    PlayerKit current = PLAYER_KITS[index];
                    String team = update.teamName == null || update.teamName.isBlank()
                            ? "Not verified" : update.teamName;
                    int number = update.number == null ? 0 : update.number;
                    PLAYER_KITS[index] = new PlayerKit(team, number, current.monogram,
                            colorForClub(team, current.primary),
                            secondaryForClub(team, current.secondary), current.pattern, false);
                    String prefix = "apiKit." + index + ".";
                    PREFS.put(prefix + "team", team);
                    PREFS.putInt(prefix + "number", number);
                    PREFS.putInt(prefix + "sourceVersion", 5);
                    PREFS.putLong(prefix + "updated", System.currentTimeMillis());
                    playerSelector.repaint();
                    if (playerSelector.getSelectedIndex() == index) {
                        updatePlayerInfo();
                        field.repaint();
                        String numberNote = update.number == null
                                ? "; shirt number not supplied by source"
                                : " · #" + number;
                        playerDataStatus.setText((team.equals("Not verified")
                                ? "ESPN roster did not supply a club"
                                : "Verified against ESPN roster: " + team)
                                + numberNote + " (checked just now).");
                    }
                } catch (CancellationException exception) {
                    return;
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    playerDataStatus.setText("Live squad lookup was interrupted.");
                } catch (ExecutionException exception) {
                    Throwable cause = exception.getCause();
                    String detail = cause == null ? exception.getMessage() : cause.getMessage();
                    playerDataStatus.setText("Live squad lookup failed: " + detail);
                } finally {
                    playerKitWorker = null;
                    if (queuedPlayerKitRefresh) {
                        queuedPlayerKitRefresh = false;
                        Timer retryTimer = new Timer(2100, event -> refreshPlayerKit(false));
                        retryTimer.setRepeats(false);
                        retryTimer.start();
                    }
                }
            }
        };
        playerKitWorker.execute();
    }

    private static PlayerKitUpdate fetchPlayerKit(String playerName)
            throws IOException, InterruptedException {
        String searchUrl = "https://site.api.espn.com/apis/search/v2?region=us&lang=en&limit=25&query="
                + URLEncoder.encode(playerName, StandardCharsets.UTF_8);
        Map<?, ?> root = asJsonObject(fetchJson(searchUrl), "ESPN search response");
        Object resultsValue = root.get("results");
        if (!(resultsValue instanceof List<?> resultGroups)) return null;

        String soughtName = normalizePlayerName(playerName);
        List<PlayerSearchCandidate> candidates = new ArrayList<>();
        for (Object groupValue : resultGroups) {
            if (!(groupValue instanceof Map<?, ?> group) || !"player".equals(group.get("type"))) continue;
            if (!(group.get("contents") instanceof List<?> contents)) continue;
            for (Object contentValue : contents) {
                if (!(contentValue instanceof Map<?, ?> content)) continue;
                if (!(content.get("displayName") instanceof String displayName)
                        || !normalizePlayerName(displayName).equals(soughtName)) continue;
                String uid = content.get("uid") instanceof String value ? value : "";
                int athleteMarker = uid.lastIndexOf("~a:");
                if (athleteMarker >= 0) candidates.add(new PlayerSearchCandidate(uid.substring(athleteMarker + 3)));
            }
        }

        for (PlayerSearchCandidate candidate : candidates) {
            String athleteUrl = "https://sports.core.api.espn.com/v2/sports/soccer/athletes/"
                    + URLEncoder.encode(candidate.athleteId, StandardCharsets.UTF_8)
                    + "?lang=en&region=us";
            Map<?, ?> athleteProfile = asJsonObject(fetchJson(athleteUrl), "ESPN player profile");
            if (!(athleteProfile.get("fullName") instanceof String profileName)
                    || !normalizePlayerName(profileName).equals(soughtName)) continue;
            Map<?, ?> teamReference = athleteProfile.get("defaultTeam") instanceof Map<?, ?> value
                    ? value : Map.of();
            Map<?, ?> leagueReference = athleteProfile.get("defaultLeague") instanceof Map<?, ?> value
                    ? value : Map.of();
            String teamId = resourceId(teamReference.get("$ref"), "teams");
            String leagueSlug = resourceId(leagueReference.get("$ref"), "leagues");
            if (teamId == null || leagueSlug == null || !isClubLeague(leagueSlug)) continue;

            String rosterUrl = "https://site.api.espn.com/apis/site/v2/sports/soccer/"
                    + URLEncoder.encode(leagueSlug, StandardCharsets.UTF_8)
                    + "/teams/" + URLEncoder.encode(teamId, StandardCharsets.UTF_8) + "/roster";
            Map<?, ?> roster = asJsonObject(fetchJson(rosterUrl), "ESPN current-season roster");
            List<?> athletes = asJsonList(roster.get("athletes"), "ESPN roster athletes");
            for (Object rosterEntry : athletes) {
                Map<?, ?> rosterPlayer = asJsonObject(rosterEntry, "ESPN roster player");
                if (!candidate.athleteId.equals(String.valueOf(rosterPlayer.get("id")))) continue;

                String teamUrl = "https://sports.core.api.espn.com/v2/sports/soccer/teams/"
                        + URLEncoder.encode(teamId, StandardCharsets.UTF_8) + "?lang=en&region=us";
                Map<?, ?> team = asJsonObject(fetchJson(teamUrl), "ESPN club profile");
                String teamName = team.get("displayName") instanceof String name ? name : null;
                Integer number = validShirtNumber(rosterPlayer.get("jersey"));
                return new PlayerKitUpdate(teamName, number);
            }
        }
        return null;
    }

    private static Object fetchJson(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(java.time.Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .header("User-Agent", "PenaltyShootout/1.0 (player roster verification)")
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            throw new IOException("ESPN returned HTTP " + response.statusCode() + ".");
        }
        return parseJson(response.body());
    }

    private static boolean isClubLeague(String leagueSlug) {
        String league = leagueSlug.toLowerCase(Locale.ROOT);
        return !league.isBlank()
                && !league.startsWith("fifa.")
                && !league.startsWith("uefa.")
                && !league.contains("friendly")
                && !league.contains("world.cup")
                && !league.contains("nations");
    }

    private static String resourceId(Object reference, String resourceName) {
        if (!(reference instanceof String url)) return null;
        String path;
        try {
            path = URI.create(url).getPath();
        } catch (IllegalArgumentException exception) {
            return null;
        }
        String[] segments = path.split("/");
        for (int index = segments.length - 2; index >= 0; index--) {
            if (resourceName.equals(segments[index]) && !segments[index + 1].isBlank()) {
                return segments[index + 1];
            }
        }
        return null;
    }

    private static final class PlayerSearchCandidate {
        private final String athleteId;

        private PlayerSearchCandidate(String athleteId) {
            this.athleteId = athleteId;
        }
    }

    private static String normalizePlayerName(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");
    }

    private static Object parseJson(String json) throws IOException {
        ApiJsonParser parser = new ApiJsonParser(json);
        Object value = parser.readValue();
        parser.skipWhitespace();
        if (parser.position != json.length()) throw new IOException("ESPN returned invalid JSON.");
        return value;
    }

    private static Map<?, ?> asJsonObject(Object value, String description) throws IOException {
        if (value instanceof Map<?, ?> object) return object;
        throw new IOException("ESPN returned an invalid " + description + ".");
    }

    private static List<?> asJsonList(Object value, String description) throws IOException {
        if (value instanceof List<?> list) return list;
        throw new IOException("ESPN returned an invalid " + description + ".");
    }

    private static Integer validShirtNumber(Object value) {
        int number = jsonInt(value, 0);
        if (number == 0 && value instanceof String jersey) {
            try {
                number = Integer.parseInt(jersey.trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return number >= 1 && number <= 99 ? number : null;
    }

    private static int jsonInt(Object value, int fallback) {
        if (value instanceof Number number) return number.intValue();
        if (value instanceof String string) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException ignored) {
                return fallback;
            }
        }
        return fallback;
    }

    private static final class ApiJsonParser {
        private final String json;
        private int position;

        private ApiJsonParser(String json) {
            this.json = json;
        }

        private void skipWhitespace() {
            while (position < json.length() && Character.isWhitespace(json.charAt(position))) position++;
        }

        private Object readValue() throws IOException {
            skipWhitespace();
            if (position >= json.length()) throw invalidJson();
            return switch (json.charAt(position)) {
                case '{' -> readObject();
                case '[' -> readArray();
                case '"' -> readString();
                case 't' -> readLiteral("true", Boolean.TRUE);
                case 'f' -> readLiteral("false", Boolean.FALSE);
                case 'n' -> readLiteral("null", null);
                default -> readNumber();
            };
        }

        private Map<String, Object> readObject() throws IOException {
            position++;
            Map<String, Object> result = new LinkedHashMap<>();
            skipWhitespace();
            if (consume('}')) return result;
            while (true) {
                skipWhitespace();
                if (position >= json.length() || json.charAt(position) != '"') throw invalidJson();
                String key = readString();
                skipWhitespace();
                if (!consume(':')) throw invalidJson();
                result.put(key, readValue());
                skipWhitespace();
                if (consume('}')) return result;
                if (!consume(',')) throw invalidJson();
            }
        }

        private List<Object> readArray() throws IOException {
            position++;
            List<Object> result = new ArrayList<>();
            skipWhitespace();
            if (consume(']')) return result;
            while (true) {
                result.add(readValue());
                skipWhitespace();
                if (consume(']')) return result;
                if (!consume(',')) throw invalidJson();
            }
        }

        private String readString() throws IOException {
            if (!consume('"')) throw invalidJson();
            StringBuilder result = new StringBuilder();
            while (position < json.length()) {
                char character = json.charAt(position++);
                if (character == '"') return result.toString();
                if (character < 0x20) throw invalidJson();
                if (character != '\\') {
                    result.append(character);
                    continue;
                }
                if (position >= json.length()) throw invalidJson();
                char escaped = json.charAt(position++);
                switch (escaped) {
                    case '"', '\\', '/' -> result.append(escaped);
                    case 'b' -> result.append('\b');
                    case 'f' -> result.append('\f');
                    case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r');
                    case 't' -> result.append('\t');
                    case 'u' -> {
                        if (position + 4 > json.length()) throw invalidJson();
                        try {
                            result.append((char) Integer.parseInt(json.substring(position, position + 4), 16));
                        } catch (NumberFormatException exception) {
                            throw invalidJson();
                        }
                        position += 4;
                    }
                    default -> throw invalidJson();
                }
            }
            throw invalidJson();
        }

        private Object readNumber() throws IOException {
            int start = position;
            if (position < json.length() && json.charAt(position) == '-') position++;
            while (position < json.length() && Character.isDigit(json.charAt(position))) position++;
            if (position < json.length() && json.charAt(position) == '.') {
                position++;
                while (position < json.length() && Character.isDigit(json.charAt(position))) position++;
            }
            if (position < json.length() && (json.charAt(position) == 'e' || json.charAt(position) == 'E')) {
                position++;
                if (position < json.length() && (json.charAt(position) == '+' || json.charAt(position) == '-')) position++;
                while (position < json.length() && Character.isDigit(json.charAt(position))) position++;
            }
            if (position == start) throw invalidJson();
            try {
                String numeric = json.substring(start, position);
                return numeric.contains(".") || numeric.contains("e") || numeric.contains("E")
                        ? Double.valueOf(numeric) : Long.valueOf(numeric);
            } catch (NumberFormatException exception) {
                throw invalidJson();
            }
        }

        private Object readLiteral(String literal, Object value) throws IOException {
            if (!json.startsWith(literal, position)) throw invalidJson();
            position += literal.length();
            return value;
        }

        private boolean consume(char expected) {
            if (position < json.length() && json.charAt(position) == expected) {
                position++;
                return true;
            }
            return false;
        }

        private IOException invalidJson() {
            return new IOException("ESPN returned malformed JSON near position " + position + ".");
        }
    }

    private static Color colorForClub(String teamName, Color fallback) {
        for (int i = 0; i < CLUBS.length; i++) {
            if (CLUBS[i].equalsIgnoreCase(teamName)) return CLUB_COLORS[i];
        }
        return fallback;
    }

    private static Color secondaryForClub(String teamName, Color fallback) {
        for (int i = 0; i < CLUBS.length; i++) {
            if (CLUBS[i].equalsIgnoreCase(teamName)) return CLUB_SECONDARY_COLORS[i];
        }
        return fallback;
    }

    private boolean editCustomPlayer() {
        JTextField nameField = new JTextField(customPlayerName, 18);
        JTextField numberField = new JTextField(Integer.toString(customPlayerNumber), 4);
        JPanel fields = new JPanel(new GridLayout(0, 2, 8, 8));
        fields.add(new JLabel("Male player name"));
        fields.add(nameField);
        fields.add(new JLabel("Shirt number"));
        fields.add(numberField);
        int result = JOptionPane.showConfirmDialog(this, fields, "Create your male player",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return false;
        String name = nameField.getText().trim();
        int number;
        try {
            number = Integer.parseInt(numberField.getText().trim());
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Enter a shirt number from 1 to 99.",
                    "Invalid shirt number", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (name.isEmpty() || name.length() > 20 || number < 1 || number > 99) {
            JOptionPane.showMessageDialog(this, "Use a name of 1–20 characters and a shirt number from 1 to 99.",
                    "Invalid player", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Color chosenColor = JColorChooser.showDialog(this, "Choose your shirt color", customPlayerColor);
        if (chosenColor != null) customPlayerColor = chosenColor;
        customPlayerName = name;
        customPlayerNumber = number;
        PREFS.put("customPlayerName", customPlayerName);
        PREFS.putInt("customPlayerNumber", customPlayerNumber);
        PREFS.putInt("customPlayerColor", customPlayerColor.getRGB());
        return true;
    }

    private static String[] playerOptions() {
        String[] options = new String[PLAYER_OPTIONS.length + 1];
        System.arraycopy(PLAYER_OPTIONS, 0, options, 0, PLAYER_OPTIONS.length);
        options[PLAYER_OPTIONS.length] = CUSTOM_PLAYER;
        return options;
    }

    private int selectedClubIndex() {
        return Math.max(0, Math.min(CLUBS.length - 1, stadiumSelector.getSelectedIndex()));
    }

    private String stadiumName() {
        return STADIUMS[selectedClubIndex()];
    }

    private String keeperNameForLevel(int level) {
        return KEEPER_OPTIONS[keeperRankForLevel(level) - 1];
    }

    private int keeperNumberForLevel(int level) {
        return KEEPER_NUMBERS[keeperRankForLevel(level) - 1];
    }

    private Color keeperColorForLevel(int level) {
        return KEEPER_KIT_COLORS[keeperRankForLevel(level) - 1];
    }

    private String keeperNationForLevel(int level) {
        return KEEPER_NATIONS[keeperRankForLevel(level) - 1];
    }

    private int keeperRankForLevel(int level) {
        return KEEPER_OPTIONS.length - Math.floorMod(level - 1, KEEPER_OPTIONS.length);
    }

    private String selectedPlayerName() {
        return CUSTOM_PLAYER.equals(playerSelector.getSelectedItem())
                ? customPlayerName : String.valueOf(playerSelector.getSelectedItem());
    }

    private static String initials(String name) {
        String[] words = name.trim().split("\\s+");
        if (words.length == 1) return words[0].substring(0, Math.min(3, words[0].length())).toUpperCase();
        return (words[0].substring(0, 1) + words[words.length - 1].substring(0, 1)).toUpperCase();
    }

    private String selectedPlayerNumber() {
        if (CUSTOM_PLAYER.equals(playerSelector.getSelectedItem())) {
            return Integer.toString(customPlayerNumber);
        }
        return selectedPlayerKit().number > 0
                ? Integer.toString(selectedPlayerKit().number) : "?";
    }

    private Color selectedShirtColor() {
        return CUSTOM_PLAYER.equals(playerSelector.getSelectedItem())
                ? customPlayerColor : selectedPlayerKit().primary;
    }

    private Color selectedSecondaryShirtColor() {
        return CUSTOM_PLAYER.equals(playerSelector.getSelectedItem())
                ? Color.WHITE : selectedPlayerKit().secondary;
    }

    private PlayerKit selectedPlayerKit() {
        int index = playerSelector.getSelectedIndex();
        return PLAYER_KITS[Math.max(0, Math.min(PLAYER_KITS.length - 1, index))];
    }

    private JPanel createGameScreen() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(NAVY);
        root.setBorder(BorderFactory.createEmptyBorder(16, 26, 18, 26));

        JPanel top = new JPanel(new BorderLayout(10, 5));
        top.setOpaque(false);
        JButton homeButton = new JButton("HOME");
        homeButton.addActionListener(event -> showHome());
        top.add(homeButton, BorderLayout.WEST);
        levelTitle.setForeground(Color.WHITE);
        levelTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        top.add(levelTitle, BorderLayout.CENTER);
        challengeLabel.setForeground(new Color(255, 209, 102));
        challengeLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        top.add(challengeLabel, BorderLayout.SOUTH);
        root.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setOpaque(false);
        progressLabel.setForeground(Color.WHITE);
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        center.add(progressLabel, BorderLayout.NORTH);
        field.setPreferredSize(new Dimension(720, 400));
        center.add(field, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(0, 1, 8, 8));
        bottom.setOpaque(false);
        statusLabel.setForeground(new Color(220, 235, 244));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        bottom.add(statusLabel);
        startButton.addActionListener(event -> startChallenge());
        retryButton.addActionListener(event -> openLevel(selectedLevel));
        nextLevelButton.addActionListener(event -> openLevel(Math.min(unlockedLevel, selectedLevel + 1)));
        JPanel actions = new JPanel();
        actions.setOpaque(false);
        for (JButton button : new JButton[]{startButton, retryButton, nextLevelButton}) {
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(38, 153, 103));
            button.setFocusPainted(false);
            actions.add(button);
        }
        startButton.setFont(new Font("SansSerif", Font.BOLD, 19));
        startButton.setPreferredSize(new Dimension(250, 52));
        bottom.add(actions);
        root.add(bottom, BorderLayout.SOUTH);
        installGameKeys(root);
        return root;
    }

    private void installGameKeys(JPanel gamePanel) {
        gamePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "aim-left");
        gamePanel.getActionMap().put("aim-left", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent event) { adjustAim(-.04, 0); }
        });
        gamePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "aim-right");
        gamePanel.getActionMap().put("aim-right", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent event) { adjustAim(.04, 0); }
        });
        gamePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "aim-up");
        gamePanel.getActionMap().put("aim-up", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent event) { adjustAim(0, -.04); }
        });
        gamePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "aim-down");
        gamePanel.getActionMap().put("aim-down", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent event) { adjustAim(0, .04); }
        });
        gamePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "shoot");
        gamePanel.getActionMap().put("shoot", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent event) { takeShot(); }
        });
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (!active || shotInProgress) return;
                aimX = Math.max(0, Math.min(1, event.getX() / (double) field.getWidth()));
                aimY = (event.getY() - field.netTop()) / (double) field.netHeight();
                field.repaint();
                takeShot();
            }
        });
    }

    private JLabel styledLabel(String text, int size, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(color);
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        return label;
    }

    private void refreshLevelBrowser() {
        levelGrid.repaint();
        levelGrid.setToolTipText("Complete levels in order to unlock the full roadmap.");
        playButton.setText("PLAY LEVEL " + unlockedLevel);
        homeProgress.setText(unlockedLevel + (unlockedLevel == 1 ? " level unlocked" : " levels unlocked"));
        updateDifficultyPreview();
        if (roadmapScroll != null) {
            SwingUtilities.invokeLater(() -> {
                int targetY = levelGrid.nodeCenterY(unlockedLevel - 1);
                int viewportHeight = roadmapScroll.getViewport().getHeight();
                int scrollY = Math.max(0, targetY - viewportHeight / 2);
                scrollY = Math.min(scrollY, Math.max(0,
                        levelGrid.getPreferredSize().height - viewportHeight));
                roadmapScroll.getViewport().setViewPosition(new java.awt.Point(0, scrollY));
            });
        }
    }

    private String challengeKind(int levelNumber) {
        if (levelNumber == 1 || (levelNumber - 1) % 4 == 0) return "TIMED";
        switch ((levelNumber - 1) % 4) {
            case 1: return "STREAK";
            case 2: return "ACCURACY";
            default: return "SEQUENCE";
        }
    }

    private Challenge makeChallenge(int number) {
        if (number == 1) return new Challenge("TIMED", 10, 15, 0, null);
        int cycle = (number - 1) / 4;
        switch ((number - 1) % 4) {
            case 0: return new Challenge("TIMED", 10 + cycle, 15 + cycle, 0, null);
            case 1: {
                int target = 3 + Math.min(cycle, 5);
                int[] sequence = cycle == 0 ? null : new int[target];
                if (sequence != null) {
                    for (int i = 0; i < target; i++) sequence[i] = (cycle / (int) Math.pow(3, i)) % 3;
                }
                return new Challenge("STREAK", target, 0, 0, sequence);
            }
            case 2: {
                int allowed = 12 + cycle;
                return new Challenge("ACCURACY", (int) Math.ceil(allowed * .65), 0, allowed, null);
            }
            default: {
                int length = 3 + cycle / 8;
                int[] sequence = new int[length];
                for (int i = 0; i < length; i++) sequence[i] = (number * 7 + i * 2 + i / 3) % 3;
                return new Challenge("SEQUENCE", length, 0, 0, sequence);
            }
        }
    }

    private JPanel createTournamentResultsScreen() {
        tournamentResultsPanel.setBackground(NAVY);
        tournamentResultsPanel.setBorder(BorderFactory.createEmptyBorder(45, 60, 45, 60));
        tournamentResultsPanel.add(tournamentResultTitle, BorderLayout.NORTH);
        JPanel center = new JPanel(new GridLayout(0, 1, 14, 14));
        center.setOpaque(false);
        center.add(tournamentResultScore);
        center.add(tournamentResultDetails);
        tournamentResultsPanel.add(center, BorderLayout.CENTER);
        JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 14, 0));
        actions.setOpaque(false);
        JButton rematch = new JButton("REMATCH");
        rematch.addActionListener(event -> startTournament());
        JButton home = new JButton("HOME");
        home.addActionListener(event -> showHome());
        for (JButton button : new JButton[]{rematch, home}) {
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(38, 153, 103));
            button.setFocusPainted(false);
            actions.add(button);
        }
        tournamentResultsPanel.add(actions, BorderLayout.SOUTH);
        return tournamentResultsPanel;
    }

    private void startTournament() {
        tournamentMode = true;
        tournamentRound = 1;
        tournamentPlayerGoals = 0;
        tournamentOpponentGoals = 0;
        tournamentPlayerKicks = 0;
        tournamentOpponentKicks = 0;
        tournamentSuddenDeath = false;
        selectedLevel = unlockedLevel;
        challenge = new Challenge("TOURNAMENT", 5, 0, 5, null);
        goals = 0;
        shots = 0;
        streak = 0;
        sequenceIndex = 0;
        active = false;
        shotInProgress = false;
        keeperDiving = false;
        keeperCaughtBall = false;
        keeperHasSecuredBall = false;
        aimX = .5;
        aimY = .38;
        lastShotResult = 0;
        soundManager.stopHomeMusic();
        startButton.setText("START SHOOTOUT");
        startButton.setVisible(true);
        retryButton.setVisible(false);
        nextLevelButton.setVisible(false);
        levelTitle.setText("PENALTY CUP");
        challengeLabel.setText(stadiumName() + "  •  Five kicks each · sudden death if tied");
        challengeLabel.setToolTipText("You kick first. The opponent responds after every kick.");
        statusLabel.setText("Choose START SHOOTOUT to begin.");
        field.setResult(0);
        updateProgress();
        field.repaint();
        cards.show(screens, "game");
    }

    private void openLevel(int number) {
        if (number < 1 || number > unlockedLevel) {
            return;
        }
        tournamentMode = false;
        if (clockTimer != null) clockTimer.stop();
        if (shotTimer != null) shotTimer.stop();
        selectedLevel = number;
        soundManager.stopHomeMusic();
        challenge = applyDifficulty(makeChallenge(selectedLevel), difficultySelector.getSelectedIndex());
        goals = 0;
        shots = 0;
        streak = 0;
        sequenceIndex = 0;
        timeLeft = challenge.seconds;
        active = false;
        shotInProgress = false;
        keeperDiving = false;
        keeperCaughtBall = false;
        keeperHasSecuredBall = false;
        diveFrame = 0;
        aimX = .5;
        aimY = .38;
        lastShotResult = 0;
        field.setResult(0);
        startButton.setVisible(true);
        startButton.setText("START CHALLENGE");
        retryButton.setVisible(false);
        nextLevelButton.setVisible(false);
        levelTitle.setText("LEVEL " + selectedLevel);
        challengeLabel.setText(describeChallenge() + "  •  " + difficultySummary()
                + "  •  FIFA #" + keeperRankForLevel(selectedLevel)
                + " " + keeperNationForLevel(selectedLevel));
        challengeLabel.setToolTipText(keeperNameForLevel(selectedLevel) + " ("
                + keeperNationForLevel(selectedLevel) + ")  •  " + difficultySummary()
                + "  •  " + stadiumName());
        statusLabel.setText("Press START CHALLENGE, then click the pitch to shoot.");
        updateProgress();
        field.repaint();
        cards.show(screens, "game");
        refreshLevelBrowser();
    }

    private String describeChallenge() {
        switch (challenge.type) {
            case "TIMED": return "Score " + challenge.target + " goals in " + challenge.seconds + " seconds";
            case "STREAK": return "Score " + challenge.target + " goals in a row";
            case "ACCURACY": return "Score " + challenge.target + " goals in " + challenge.shotsAllowed + " shots";
            case "SEQUENCE": return "Score " + challenge.target + " goals in order: " + sequenceText();
            default: throw new IllegalStateException("Unknown challenge: " + challenge.type);
        }
    }

    private String sequenceText() {
        StringBuilder text = new StringBuilder();
        for (int zone : challenge.sequence) {
            if (text.length() > 0) text.append(" → ");
            text.append(ZONES[zone]);
        }
        return text.toString();
    }

    private void startChallenge() {
        if (active) return;
        goals = 0;
        shots = 0;
        streak = 0;
        sequenceIndex = 0;
        timeLeft = challenge.seconds;
        active = true;
        shotInProgress = false;
        startButton.setVisible(false);
        retryButton.setVisible(false);
        nextLevelButton.setVisible(false);
        statusLabel.setText(tournamentMode
                ? "You kick first. Click inside the goal or use the arrow keys and Space."
                : "Click inside the goal to shoot. Use arrow keys to aim and Space to shoot.");
        soundManager.playWhistle();
        soundManager.startCrowd();
        updateProgress();
        if ("TIMED".equals(challenge.type)) {
            long deadline = System.currentTimeMillis() + challenge.seconds * 1000L;
            if (clockTimer != null) clockTimer.stop();
            clockTimer = new Timer(100, event -> {
                timeLeft = Math.max(0, (int) Math.ceil((deadline - System.currentTimeMillis()) / 1000.0));
                updateProgress();
                if (timeLeft == 0 && !shotInProgress) finishLevel(false);
            });
            clockTimer.start();
        }
    }

    private void adjustAim(double dx, double dy) {
        if (!active || shotInProgress) return;
        aimX = Math.max(.03, Math.min(.97, aimX + dx));
        aimY = Math.max(.02, Math.min(.96, aimY + dy));
        field.repaint();
    }

    private void takeShot() {
        if (!active || shotInProgress) return;
        shotInProgress = true;
        shots++;
        shotStartedAt = System.currentTimeMillis();
        soundManager.playKick();
        ballStartX = .5;
        ballStartY = .9;
        ballTargetX = aimX;
        ballTargetY = aimY;
        ballX = ballStartX;
        ballY = ballStartY;
        field.setAimVisible(false);

        double netLeft = field.netLeft();
        double netRight = field.netRight();
        double netPosition = (aimX - netLeft) / (netRight - netLeft);
        int targetZone = netPosition < 1.0 / 3 ? 0 : netPosition > 2.0 / 3 ? 2 : 1;
        boolean insideNet = aimX > netLeft && aimX < netRight && aimY > 0 && aimY < 1;
        int heightZone = aimY < .32 ? 0 : 1;
        boolean requiredZoneWrong = false;
        int requiredZone = -1;
        if ("SEQUENCE".equals(challenge.type) && sequenceIndex < challenge.sequence.length) {
            requiredZone = challenge.sequence[sequenceIndex];
            requiredZoneWrong = targetZone != requiredZone;
        } else if ("STREAK".equals(challenge.type) && challenge.sequence != null && streak < challenge.sequence.length) {
            requiredZone = challenge.sequence[streak];
            requiredZoneWrong = targetZone != requiredZone;
        }
        keeperZone = targetZone;
        keeperStartX = keeperX;
        keeperTargetX = Math.max(netLeft + .025, Math.min(netRight - .025, aimX));
        diveDirection = targetZone == 0 ? -1 : targetZone == 2 ? 1 : 0;
        keeperDiving = insideNet;
        keeperCaughtBall = false;
        keeperHasSecuredBall = false;
        keeperDiveStartedAt = shotStartedAt;
        diveFrame = 0;
        lastShotResult = 0;
        boolean keeperSaved = false;
        ballTargetY = (field.netTop() + aimY * field.netHeight()) / (double) field.getHeight();
        field.repaint();

        if (!insideNet) {
            streak = 0;
            lastShotResult = -2;
            statusLabel.setText("WIDE! Aim between the posts and below the crossbar.");
            soundManager.playMiss();
        } else {
            boolean saved = random.nextDouble() < saveChance(keeperStartX, keeperTargetX, heightZone);
            keeperSaved = saved && !requiredZoneWrong;
            if (requiredZoneWrong) {
                streak = 0;
                lastShotResult = -3;
                statusLabel.setText("WRONG ZONE! Aim for " + ZONES[requiredZone] + " next.");
                soundManager.playMiss();
            } else if (saved) {
                streak = 0;
                lastShotResult = -1;
                keeperCaughtBall = true;
                statusLabel.setText("SAVED! " + keeperNameForLevel(selectedLevel) + " ("
                        + keeperNationForLevel(selectedLevel) + ")"
                        + " got across to " + ZONES[targetZone].toLowerCase() + ".");
                soundManager.playSave();
            } else {
                goals++;
                streak++;
                lastShotResult = 1;
                if ("SEQUENCE".equals(challenge.type)) sequenceIndex++;
                statusLabel.setText("GOAL! You beat " + keeperNameForLevel(selectedLevel)
                        + " (" + keeperNationForLevel(selectedLevel) + ").");
                soundManager.playGoal();
            }
        }
        if (tournamentMode) {
            tournamentPlayerKicks++;
            if (lastShotResult == 1) tournamentPlayerGoals++;
        }
        recordShotStatistics(insideNet, keeperSaved);
        statusLabel.setText(statusLabel.getText() + " · " + keeperRecordSummary(selectedLevel));
        updateProgress();

        if (shotTimer != null) shotTimer.stop();
        shotTimer = new Timer(16, event -> {
            double progress = Math.min(1, (System.currentTimeMillis() - shotStartedAt) / 520.0);
            if (keeperCaughtBall && progress > .64) {
                double catchProgress = Math.min(1, (progress - .64) / .2);
                double catchEased = 1 - Math.pow(1 - catchProgress, 3);
                double catchX = field.keeperCatchX(keeperX, diveDirection);
                double catchY = field.keeperCatchY();
                ballX = ballTargetX + (catchX - ballTargetX) * catchEased;
                ballY = ballTargetY + (catchY - ballTargetY) * catchEased;
            } else {
                double flightProgress = keeperCaughtBall ? Math.min(1, progress / .64) : progress;
                double eased = 1 - Math.pow(1 - flightProgress, 3);
                ballX = ballStartX + (ballTargetX - ballStartX) * eased;
                ballY = ballStartY + (ballTargetY - ballStartY) * eased;
            }
            if (keeperDiving) {
                double keeperProgress = Math.min(1,
                        (System.currentTimeMillis() - keeperDiveStartedAt) / (double) keeperDiveDuration());
                double keeperEased = 1 - Math.pow(1 - keeperProgress, 3);
                diveFrame = (int) (keeperProgress * 100);
                keeperX = keeperStartX + (keeperTargetX - keeperStartX) * keeperEased;
            }
            if (keeperCaughtBall && progress >= .84) {
                keeperHasSecuredBall = true;
                ballX = field.keeperCatchX(keeperX, diveDirection);
                ballY = field.keeperCatchY();
            }
            field.repaint();
            if (progress >= 1) {
                shotTimer.stop();
                shotInProgress = false;
                field.setAimVisible(true);
                field.setResult(lastShotResult);
                updateProgress();
                checkChallengeEnd();
                if (active) statusLabel.setText("Click the pitch to take your next shot.");
            }
        });
        shotTimer.start();
    }

    private double saveChance(double startX, double targetX, int heightZone) {
        double travel = Math.abs(startX - targetX);
        double chance = DIFFICULTY_SAVE_RATES[difficultySelector.getSelectedIndex()]
                * Math.max(.82, 1 - travel * .7);
        if (heightZone == 0) chance *= .9;
        if (challenge.type.equals("STREAK") || challenge.type.equals("SEQUENCE")) chance *= .95;
        PenaltyRecord shooterRecord = selectedPenaltyRecord();
        if (shooterRecord != null) {
            double conversionAdjustment = Math.max(.9,
                    Math.min(1.1, 1 + (.8 - shooterRecord.conversionRate()) * .5));
            chance *= conversionAdjustment;
        }
        int keeperIndex = keeperRankForLevel(selectedLevel) - 1;
        String keeperKey = keeperStatsKey(keeperIndex);
        int keeperFaced = PREFS.getInt(keeperKey + ".faced", 0);
        if (keeperFaced > 0) {
            double observedSaveRate = PREFS.getInt(keeperKey + ".saves", 0) / (double) keeperFaced;
            chance *= Math.max(.9, Math.min(1.1, 1 + (observedSaveRate - .2) * .2));
        }
        return Math.max(.01, Math.min(.95, chance));
    }

    private String keeperRecordSummary(int level) {
        int keeperIndex = keeperRankForLevel(level) - 1;
        String key = keeperStatsKey(keeperIndex);
        int saves = PREFS.getInt(key + ".saves", 0);
        int faced = PREFS.getInt(key + ".faced", 0);
        return faced == 0 ? "Keeper: 0 saves"
                : String.format("%s saves %d/%d (%.1f%%)",
                        keeperNameForLevel(level), saves, faced, saves * 100.0 / faced);
    }

    private PenaltyRecord selectedPenaltyRecord() {
        Object selected = playerSelector.getSelectedItem();
        if (selected == null || CUSTOM_PLAYER.equals(selected)) return null;
        PenaltyRecord record = PENALTY_RECORDS.get(selected.toString());
        if (record == null) {
            throw new IllegalStateException("Missing penalty rating for selected taker: " + selected);
        }
        return record;
    }

    private int keeperDiveDuration() {
        int[] reactionTimes = {650, 430, 300, 190, 100};
        return reactionTimes[difficultySelector.getSelectedIndex()];
    }

    private String selectedDifficulty() {
        return (String) difficultySelector.getSelectedItem();
    }

    private void updateDifficultyColor() {
        difficultySelector.setBackground(DIFFICULTY_COLORS[difficultySelector.getSelectedIndex()]);
        difficultySelector.setForeground(Color.WHITE);
    }

    private void updateDifficultyPreview() {
        if (difficultyPreview == null) return;
        Challenge preview = applyDifficulty(makeChallenge(unlockedLevel), difficultySelector.getSelectedIndex());
        int savePercent = (int) Math.round(DIFFICULTY_SAVE_RATES[difficultySelector.getSelectedIndex()] * 100);
        difficultyPreview.setText("<html><center>NEXT: " + describeChallenge(preview)
                + "<br>Keeper: ~" + savePercent + "% saves, " + formatReactionTime() + " reaction</center></html>");
    }

    private String difficultySummary() {
        int savePercent = (int) Math.round(DIFFICULTY_SAVE_RATES[difficultySelector.getSelectedIndex()] * 100);
        return selectedDifficulty().toUpperCase() + "  •  KEEPER ~" + savePercent + "% SAVES / "
                + formatReactionTime() + " REACTION";
    }

    private String formatReactionTime() {
        return String.format("%.2f s", keeperDiveDuration() / 1000.0);
    }

    private String describeChallenge(Challenge currentChallenge) {
        switch (currentChallenge.type) {
            case "TIMED": return "Score " + currentChallenge.target + " goals in " + currentChallenge.seconds + " seconds";
            case "STREAK": return "Score " + currentChallenge.target + " goals in a row";
            case "ACCURACY": return "Score " + currentChallenge.target + " goals in "
                    + currentChallenge.shotsAllowed + " shots";
            case "SEQUENCE": return "Score " + currentChallenge.target + " goals in order";
            default: throw new IllegalStateException("Unknown challenge: " + currentChallenge.type);
        }
    }

    private Challenge applyDifficulty(Challenge base, int difficulty) {
        double[] targetFactors = {.5, 1, 1.5, 2, 2.5};
        int target = Math.max(1, (int) Math.round(base.target * targetFactors[difficulty]));
        if ("TIMED".equals(base.type)) {
            double[] timeFactors = {1.5, 1, .8, .65, .5};
            int seconds = Math.max(5, (int) Math.round(base.seconds * timeFactors[difficulty]));
            return new Challenge(base.type, target, seconds, base.shotsAllowed, base.sequence);
        }
        if ("ACCURACY".equals(base.type)) {
            double[] attemptFactors = {1.5, 1, .8, .65, .5};
            int attempts = Math.max(1, (int) Math.round(base.shotsAllowed * attemptFactors[difficulty]));
            double[] accuracyFactors = {.55, 1, 1.4, 1.8, 2.2};
            target = Math.max(1, (int) Math.round(base.target * accuracyFactors[difficulty]));
            target = Math.min(target, attempts);
            return new Challenge(base.type, target, base.seconds, attempts, base.sequence);
        }
        int[] sequence = base.sequence;
        if (sequence != null) {
            int[] adjustedSequence = new int[target];
            for (int i = 0; i < target; i++) adjustedSequence[i] = sequence[i % sequence.length];
            sequence = adjustedSequence;
        }
        return new Challenge(base.type, target, base.seconds, base.shotsAllowed, sequence);
    }

    private void checkChallengeEnd() {
        if (tournamentMode) {
            resolveTournamentReply();
            return;
        }
        boolean won = switch (challenge.type) {
            case "TIMED" -> goals >= challenge.target;
            case "STREAK" -> streak >= challenge.target;
            case "ACCURACY" -> goals >= challenge.target;
            case "SEQUENCE" -> sequenceIndex >= challenge.target;
            default -> false;
        };
        if (won) {
            finishLevel(true);
        } else if ("ACCURACY".equals(challenge.type) && shots >= challenge.shotsAllowed) {
            finishLevel(false);
        } else if ("TIMED".equals(challenge.type) && timeLeft <= 0) {
            finishLevel(false);
        }
    }

    private void resolveTournamentReply() {
        active = false;
        statusLabel.setText("The rivals step up for their penalty…");
        updateProgress();
        if (tournamentReplyTimer != null) tournamentReplyTimer.stop();
        tournamentReplyTimer = new Timer(850, event -> {
            tournamentReplyTimer.stop();
            active = true;
            tournamentOpponentKicks++;
            double opponentConversion = .78 - difficultySelector.getSelectedIndex() * .075;
            boolean rivalScored = random.nextDouble() < opponentConversion;
            if (rivalScored) tournamentOpponentGoals++;
            if (tournamentSuddenDeath) {
                statusLabel.setText(rivalScored
                        ? "Rivals score in sudden death. Your turn."
                        : "Rivals miss in sudden death. Your turn.");
            } else {
                statusLabel.setText(rivalScored
                        ? "Rivals convert. Your turn."
                        : "Rivals miss. Your turn.");
            }
            updateProgress();
            if (tournamentRoundComplete()) {
                finishTournament();
            } else {
                if (tournamentSuddenDeath) {
                    tournamentRound++;
                } else if (tournamentRound >= 5) {
                    tournamentSuddenDeath = true;
                    tournamentRound++;
                } else {
                    tournamentRound++;
                }
                statusLabel.setText(tournamentSuddenDeath
                        ? "Sudden death · you kick first."
                        : "Round " + tournamentRound + " · you kick first.");
                updateProgress();
            }
        });
        tournamentReplyTimer.setRepeats(false);
        tournamentReplyTimer.start();
    }

    private boolean tournamentRoundComplete() {
        int difference = Math.abs(tournamentPlayerGoals - tournamentOpponentGoals);
        if (tournamentSuddenDeath) return difference > 0;
        int roundsRemaining = 5 - tournamentRound;
        if (difference > roundsRemaining) return true;
        return tournamentRound == 5 && difference > 0;
    }

    private void finishTournament() {
        active = false;
        if (tournamentReplyTimer != null) tournamentReplyTimer.stop();
        soundManager.stopCrowd();
        String result = tournamentPlayerGoals > tournamentOpponentGoals
                ? "YOU WIN THE SHOOTOUT"
                : tournamentPlayerGoals < tournamentOpponentGoals
                        ? "RIVALS WIN THE SHOOTOUT" : "SHOOTOUT DRAW";
        tournamentResultTitle.setText(result);
        tournamentResultScore.setText("<html><center>YOU &nbsp; " + tournamentPlayerGoals
                + " &nbsp;–&nbsp; " + tournamentOpponentGoals + " &nbsp; RIVALS</center></html>");
        PenaltyRecord takerRecord = selectedPenaltyRecord();
        String takerLine = takerRecord == null
                ? selectedPlayerName() + " · custom player; no real-life penalty record"
                : String.format("%s · real-life penalties %d/%d (%.1f%%)",
                        selectedPlayerName(), takerRecord.scored, takerRecord.attempts(),
                        takerRecord.conversionRate() * 100);
        int keeperIndex = keeperRankForLevel(selectedLevel) - 1;
        String keeperKey = keeperStatsKey(keeperIndex);
        int saves = PREFS.getInt(keeperKey + ".saves", 0);
        int faced = PREFS.getInt(keeperKey + ".faced", 0);
        String keeperLine = String.format("%s · in-game saves %d/%d%s",
                keeperNameForLevel(selectedLevel), saves, faced,
                faced == 0 ? "" : String.format(" (%.1f%%)", saves * 100.0 / faced));
        tournamentResultDetails.setText("<html><center>"
                + "Rounds: " + (tournamentSuddenDeath ? "5 + sudden death" : tournamentRound)
                + "<br>" + takerLine
                + "<br>" + keeperLine
                + "</center></html>");
        cards.show(screens, "tournament-results");
    }

    private void finishLevel(boolean won) {
        active = false;
        if (clockTimer != null) clockTimer.stop();
        soundManager.stopCrowd();
        startButton.setVisible(false);
        if (won) {
            statusLabel.setText("LEVEL COMPLETE! You scored " + goals + " goals.");
            if (selectedLevel == highestCompletedLevel + 1) {
                highestCompletedLevel = selectedLevel;
                unlockedLevel = Math.min(MAX_LEVEL, highestCompletedLevel + 1);
                PREFS.putInt("highestCompletedLevel", highestCompletedLevel);
                PREFS.putInt("unlockedLevel", unlockedLevel);
            }
            retryButton.setVisible(false);
            nextLevelButton.setVisible(selectedLevel < MAX_LEVEL);
            nextLevelButton.setEnabled(selectedLevel < unlockedLevel);
            refreshLevelBrowser();
        } else {
            statusLabel.setText("CHALLENGE OVER. You scored " + goals + " goals. Try again!");
            retryButton.setVisible(true);
            nextLevelButton.setVisible(false);
        }
        updateProgress();
    }

    private void updateProgress() {
        if (tournamentMode) {
            String round = tournamentSuddenDeath
                    ? "SUDDEN DEATH " + Math.max(1, tournamentRound - 5)
                    : "ROUND " + tournamentRound + "/5";
            progressLabel.setText(round + "     YOU  " + tournamentPlayerGoals
                    + "  –  " + tournamentOpponentGoals + "  RIVALS"
                    + "     KICKS " + tournamentPlayerKicks + "–" + tournamentOpponentKicks);
            return;
        }
        String summary = "GOALS  " + goals;
        if ("TIMED".equals(challenge.type)) summary += "     TIME  " + timeLeft + "s";
        if ("STREAK".equals(challenge.type)) summary += "     STREAK  " + streak + "/" + challenge.target;
        if ("ACCURACY".equals(challenge.type)) summary += "     SHOTS LEFT  " + Math.max(0, challenge.shotsAllowed - shots);
        if ("SEQUENCE".equals(challenge.type)) summary += "     TARGET  " + Math.min(sequenceIndex + 1, challenge.target)
                + "/" + challenge.target;
        progressLabel.setText(summary);
    }

    private void showHome() {
        tournamentMode = false;
        if (tournamentReplyTimer != null) tournamentReplyTimer.stop();
        active = false;
        shotInProgress = false;
        keeperDiving = false;
        keeperCaughtBall = false;
        keeperHasSecuredBall = false;
        diveFrame = 0;
        if (clockTimer != null) clockTimer.stop();
        if (shotTimer != null) shotTimer.stop();
        soundManager.stopCrowd();
        refreshLevelBrowser();
        homeTabs.setSelectedIndex(0);
        cards.show(screens, "home");
        soundManager.startHomeMusic();
    }

    private final class HomeHeroPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private HomeHeroPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            int width = getWidth();
            int height = getHeight();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(new GradientPaint(0, 0, new Color(20, 71, 76),
                    width, height, new Color(12, 31, 49)));
            g.fillRoundRect(0, 0, width, height, 22, 22);

            float radius = Math.max(100, height * 1.3f);
            g.setPaint(new RadialGradientPaint(new Point2D.Double(width * .5, height * .08),
                    radius, new float[]{0f, 1f},
                    new Color[]{new Color(88, 211, 154, 72), new Color(25, 76, 88, 0)}));
            g.fillRoundRect(0, 0, width, height, 22, 22);

            g.setColor(new Color(218, 255, 237, 20));
            g.setStroke(new BasicStroke(1f));
            for (int i = 0; i < 7; i++) {
                int y = height - 12 - i * 13;
                g.drawLine(18, y, width - 18, y);
            }
            g.setColor(new Color(164, 237, 202, 34));
            g.drawArc(width / 2 - 150, height - 42, 300, 84, 0, 180);
            g.drawArc(width / 2 - 225, height - 64, 450, 128, 0, 180);
            g.dispose();
        }
    }

    private final class StadiumPreviewPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private StadiumPreviewPanel() {
            setPreferredSize(new Dimension(620, 300));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            int width = getWidth();
            int height = getHeight();
            Color club = CLUB_COLORS[selectedClubIndex()];
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(new GradientPaint(0, 0, new Color(36, 58, 77),
                    0, height, new Color(12, 41, 42)));
            g.fillRoundRect(0, 0, width, height, 24, 24);

            Color secondary = CLUB_SECONDARY_COLORS[selectedClubIndex()];
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRoundRect(19, 52, width - 38, height - 76, 58, 58);
            g.setPaint(new GradientPaint(0, 50, secondary.darker(),
                    0, 108, new Color(12, 20, 33)));
            g.fillRoundRect(22, 38, width - 44, 84, 48, 48);
            g.setColor(new Color(235, 246, 247, 80));
            g.setStroke(new BasicStroke(3));
            g.drawArc(30, 14, width - 60, 120, 0, 180);
            g.drawArc(56, 34, width - 112, 92, 0, 180);

            g.setColor(club.darker());
            g.fillPolygon(new int[]{20, width / 4, width / 3, width / 5},
                    new int[]{78, 96, 184, 211}, 4);
            g.fillPolygon(new int[]{width - 20, width * 3 / 4, width * 2 / 3, width * 4 / 5},
                    new int[]{78, 96, 184, 211}, 4);
            g.setColor(new Color(club.getRed(), club.getGreen(), club.getBlue(), 205));
            g.fillPolygon(new int[]{width / 4, width * 3 / 4, width * 2 / 3, width / 3},
                    new int[]{82, 82, 190, 190}, 4);
            g.setColor(new Color(0, 0, 0, 55));
            g.fillPolygon(new int[]{width / 4, width * 3 / 4, width * 3 / 4, width / 4},
                    new int[]{130, 130, 153, 153}, 4);

            int quality = Math.max(0, Math.min(2, graphicsSelector.getSelectedIndex()));
            int crowdRows = quality == 0 ? 3 : quality == 1 ? 5 : 8;
            Random crowd = new Random(selectedClubIndex() * 31L + 7);
            for (int row = 0; row < crowdRows; row++) {
                int y = 66 + row * 12;
                for (int x = 42; x < width - 42; x += quality == 2 ? 8 : 13) {
                    int variation = crowd.nextInt(50);
                    g.setColor(new Color(Math.min(255, 160 + variation),
                            Math.min(255, 166 + variation), Math.min(255, 173 + variation), 190));
                    g.fillOval(x, y, 3, 4);
                }
            }

            int fieldTop = 127;
            int fieldBottom = height - 26;
            int[] fieldX = {width / 3, width * 2 / 3, width - 30, 30};
            int[] fieldY = {fieldTop, fieldTop, fieldBottom, fieldBottom};
            g.setPaint(new GradientPaint(0, fieldTop, new Color(58, 153, 92),
                    0, fieldBottom, new Color(13, 92, 55)));
            g.fillPolygon(fieldX, fieldY, 4);
            Graphics2D field = (Graphics2D) g.create();
            field.setClip(new java.awt.Polygon(fieldX, fieldY, 4));
            field.setColor(new Color(211, 249, 212, 28));
            for (int stripe = 0; stripe < 6; stripe++) {
                int stripeY = fieldTop + (fieldBottom - fieldTop) * stripe / 6;
                field.fillRect(0, stripeY, width, Math.max(3, (fieldBottom - fieldTop) / 12));
            }
            field.setColor(new Color(232, 250, 233, 175));
            field.setStroke(new BasicStroke(1.4f));
            field.drawLine(width / 2, fieldTop, width / 2, fieldBottom);
            field.drawOval(width / 2 - 19, fieldTop + (fieldBottom - fieldTop) / 2 - 16, 38, 32);
            field.drawPolygon(fieldX, fieldY, 4);
            field.drawLine(width / 3 + 8, fieldTop + 4, width * 2 / 3 - 8, fieldTop + 4);
            field.dispose();

            g.setColor(new Color(255, 255, 255, 220));
            g.setFont(new Font("SansSerif", Font.BOLD, 13));
            FontMetrics metrics = g.getFontMetrics();
            String venue = stadiumName().toUpperCase();
            g.drawString(venue, (width - metrics.stringWidth(venue)) / 2, height - 22);
            g.dispose();
        }
    }

    private final class SoundManager {
        private static final float SAMPLE_RATE = 22050f;
        private static final AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        private Clip crowdClip;
        private Clip homeMusicClip;
        private boolean crowdRequested;
        private int gameVolume = 75;
        private int stadiumVolume = 45;

        private void setGameVolume(int volume) {
            gameVolume = Math.max(0, Math.min(100, volume));
            setClipVolume(homeMusicClip, gameVolume);
        }

        private void setStadiumVolume(int volume) {
            stadiumVolume = Math.max(0, Math.min(100, volume));
            if (crowdRequested) {
                closeCrowdClip();
                if (stadiumVolume > 0) startCrowd();
            }
        }

        private void previewGame() {
            playKick();
        }

        private void previewCrowd() {
            playCrowdCheer(stadiumVolume);
        }

        private void startHomeMusic() {
            if (homeMusicClip != null && homeMusicClip.isOpen()) return;
            try {
                byte[] audio = createHomeMusic();
                homeMusicClip = AudioSystem.getClip();
                homeMusicClip.open(FORMAT, audio, 0, audio.length);
                setClipVolume(homeMusicClip, gameVolume);
                homeMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (LineUnavailableException exception) {
                homeMusicClip = null;
                reportAudioFailure(exception);
            }
        }

        private void stopHomeMusic() {
            if (homeMusicClip == null) return;
            homeMusicClip.stop();
            homeMusicClip.close();
            homeMusicClip = null;
        }

        private void setClipVolume(Clip clip, int volume) {
            if (clip == null || !clip.isOpen() || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                return;
            }
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float decibels = volume == 0 ? gain.getMinimum()
                    : (float) (20 * Math.log10(volume / 100.0));
            gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), decibels)));
        }

        private byte[] createHomeMusic() {
            int seconds = 8;
            int samples = (int) (SAMPLE_RATE * seconds);
            byte[] audio = new byte[samples * 2];
            double[] melody = {261.63, 329.63, 392.00, 329.63, 293.66, 349.23, 440.00, 349.23,
                    261.63, 329.63, 392.00, 523.25, 293.66, 349.23, 440.00, 349.23};
            double[] bass = {130.81, 130.81, 146.83, 146.83, 110.00, 110.00, 146.83, 146.83};
            for (int i = 0; i < samples; i++) {
                double time = i / SAMPLE_RATE;
                int beat = (int) (time * 2) % melody.length;
                double beatProgress = time * 2 - Math.floor(time * 2);
                double noteEnvelope = Math.exp(-beatProgress * 2.3);
                double lead = Math.sin(2 * Math.PI * melody[beat] * time)
                        + .32 * Math.sin(2 * Math.PI * melody[beat] * 2 * time);
                double pad = Math.sin(2 * Math.PI * melody[beat] * .5 * time);
                double bassPulse = Math.exp(-beatProgress * 7)
                        * Math.sin(2 * Math.PI * bass[(int) time % bass.length] * time);
                double value = (.12 * lead * noteEnvelope + .055 * pad + .12 * bassPulse) * .55;
                short sample = (short) Math.max(Short.MIN_VALUE,
                        Math.min(Short.MAX_VALUE, value * 32767));
                audio[i * 2] = (byte) (sample & 0xff);
                audio[i * 2 + 1] = (byte) ((sample >>> 8) & 0xff);
            }
            return audio;
        }

        private void playKick() {
            playImpact(.13, gameVolume, .9);
            playTone(new double[]{78, 52}, .17, gameVolume, .38);
        }

        private void playGoal() {
            playTone(new double[]{659, 784}, .36, gameVolume, .16);
            playCrowdCheer(stadiumVolume);
        }

        private void playSave() {
            playImpact(.11, gameVolume, .5);
            playTone(new double[]{196, 155, 130}, .3, gameVolume, .38);
        }

        private void playMiss() {
            playImpact(.08, gameVolume, .34);
        }

        private void playWhistle() {
            playTone(new double[]{1300, 1580}, .44, gameVolume, .18);
        }

        private void startCrowd() {
            crowdRequested = true;
            if (stadiumVolume == 0 || crowdClip != null && crowdClip.isOpen()) return;
            byte[] audio = createCrowdAudio(4.0, stadiumVolume, false);
            try {
                crowdClip = AudioSystem.getClip();
                crowdClip.open(FORMAT, audio, 0, audio.length);
                crowdClip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (LineUnavailableException exception) {
                crowdClip = null;
                reportAudioFailure(exception);
            }
        }

        private void playCrowdCheer(int volume) {
            if (volume == 0) return;
            playAudio(createCrowdAudio(2.3, volume, true), false);
        }

        private byte[] createCrowdAudio(double seconds, int volume, boolean cheering) {
            int samples = Math.max(1, (int) (SAMPLE_RATE * seconds));
            byte[] audio = new byte[samples * 2];
            Random noise = new Random();
            double filteredNoise = 0;
            double[] voiceFrequency = {92, 127, 164, 203, 247, 292, 351, 418};
            double[] voicePhase = new double[voiceFrequency.length];
            double[] voiceRate = new double[voiceFrequency.length];
            for (int voice = 0; voice < voiceFrequency.length; voice++) {
                voicePhase[voice] = noise.nextDouble() * Math.PI * 2;
                voiceRate[voice] = .45 + noise.nextDouble() * 1.35;
            }
            for (int i = 0; i < samples; i++) {
                double time = i / SAMPLE_RATE;
                filteredNoise = filteredNoise * .82 + (noise.nextDouble() * 2 - 1) * .18;
                double voices = 0;
                for (int voice = 0; voice < voiceFrequency.length; voice++) {
                    double modulation = .28 + .72 * Math.max(0,
                            Math.sin(2 * Math.PI * voiceRate[voice] * time + voicePhase[voice]));
                    double formant = Math.sin(2 * Math.PI * voiceFrequency[voice] * time
                            + voicePhase[voice]) * modulation;
                    voices += formant / voiceFrequency.length;
                }
                double swell = .58 + .28 * Math.sin(2 * Math.PI * time / (cheering ? 1.65 : 3.6));
                double ramp = cheering
                        ? Math.min(1, time * 5) * Math.min(1, Math.max(.12, (seconds - time) * 1.5))
                        : 1;
                double applause = cheering && noise.nextDouble() < .035 ? (noise.nextDouble() * 2 - 1) : 0;
                double sampleValue = (filteredNoise * .42 + voices * (cheering ? .7 : .38) + applause * .18)
                        * swell * ramp * volume / 100.0;
                short sample = (short) Math.max(Short.MIN_VALUE,
                        Math.min(Short.MAX_VALUE, sampleValue * 18500));
                audio[i * 2] = (byte) (sample & 0xff);
                audio[i * 2 + 1] = (byte) ((sample >>> 8) & 0xff);
            }
            return audio;
        }

        private void playImpact(double seconds, int volume, double strength) {
            if (volume == 0) return;
            int samples = Math.max(1, (int) (SAMPLE_RATE * seconds));
            byte[] audio = new byte[samples * 2];
            Random noise = new Random();
            double lowPass = 0;
            for (int i = 0; i < samples; i++) {
                double time = i / SAMPLE_RATE;
                lowPass = lowPass * .72 + (noise.nextDouble() * 2 - 1) * .28;
                double envelope = Math.exp(-time * 34);
                double thump = Math.sin(2 * Math.PI * (68 - time * 80) * time);
                double value = (lowPass * .78 + thump * .42) * envelope * strength * volume / 100.0;
                short sample = (short) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, value * 24500));
                audio[i * 2] = (byte) (sample & 0xff);
                audio[i * 2 + 1] = (byte) ((sample >>> 8) & 0xff);
            }
            playAudio(audio, false);
        }

        private void playAudio(byte[] audio, boolean loop) {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(FORMAT, audio, 0, audio.length);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.addLineListener(event -> {
                        if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP && clip.isOpen()) {
                            clip.close();
                        }
                    });
                    clip.start();
                }
            } catch (LineUnavailableException exception) {
                reportAudioFailure(exception);
            }
        }

        private void stopCrowd() {
            crowdRequested = false;
            closeCrowdClip();
        }

        private void closeCrowdClip() {
            if (crowdClip == null) return;
            crowdClip.stop();
            crowdClip.close();
            crowdClip = null;
        }

        private void playTone(double[] frequencies, double seconds, int volume, double strength) {
            if (volume == 0) return;
            int samples = Math.max(1, (int) (SAMPLE_RATE * seconds));
            byte[] audio = new byte[samples * 2];
            for (int i = 0; i < samples; i++) {
                double progress = i / (double) samples;
                double envelope = Math.min(1, progress * 24) * Math.pow(1 - progress, .8);
                double value = 0;
                for (int tone = 0; tone < frequencies.length; tone++) {
                    value += Math.sin(2 * Math.PI * frequencies[tone] * i / SAMPLE_RATE + tone * .2)
                            / frequencies.length;
                }
                short sample = (short) (value * envelope * strength * volume / 100.0 * 24000);
                audio[i * 2] = (byte) (sample & 0xff);
                audio[i * 2 + 1] = (byte) ((sample >>> 8) & 0xff);
            }
            playAudio(audio, false);
        }

        private void reportAudioFailure(LineUnavailableException exception) {
            String message = "Audio output unavailable: " + exception.getMessage();
            settingsInfo.setText(message);
            statusLabel.setText(message);
        }
    }

    private static final class PlayerKit {
        private final String team;
        private final int number;
        private final String monogram;
        private final Color primary;
        private final Color secondary;
        private final int pattern;
        private final boolean retired;

        private PlayerKit(String team, int number, String monogram, Color primary,
                Color secondary, int pattern, boolean retired) {
            this.team = team;
            this.number = number;
            this.monogram = monogram;
            this.primary = primary;
            this.secondary = secondary;
            this.pattern = pattern;
            this.retired = retired;
        }
    }

    private static final class PenaltyRecord {
        private final int scored;
        private final int missed;
        private final String sourceUrl;

        private PenaltyRecord(int scored, int missed, String sourceUrl) {
            if (scored < 0 || missed < 0 || scored + missed == 0) {
                throw new IllegalArgumentException("Penalty record must contain attempts.");
            }
            this.scored = scored;
            this.missed = missed;
            this.sourceUrl = sourceUrl;
        }

        private int attempts() {
            return scored + missed;
        }

        private double conversionRate() {
            return scored / (double) attempts();
        }
    }

    private static final class PlayerKitUpdate {
        private final String teamName;
        private final Integer number;

        private PlayerKitUpdate(String teamName, Integer number) {
            this.teamName = teamName;
            this.number = number;
        }
    }

    private static final class GearIcon implements javax.swing.Icon {
        private static final int SIZE = 24;

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.translate(x, y);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            for (int tooth = 0; tooth < 8; tooth++) {
                Graphics2D spoke = (Graphics2D) g.create();
                spoke.rotate(tooth * Math.PI / 4, SIZE / 2.0, SIZE / 2.0);
                spoke.fillRoundRect(SIZE / 2 - 2, 1, 4, 8, 2, 2);
                spoke.dispose();
            }
            g.fillOval(3, 3, SIZE - 6, SIZE - 6);
            g.setColor(new Color(40, 61, 82));
            g.fillOval(8, 8, SIZE - 16, SIZE - 16);
            g.dispose();
        }
    }

    private static final class PlayerPortraitIcon implements javax.swing.Icon {
        private final String name;
        private final int number;
        private final Color shirt;
        private final Color secondary;
        private final String teamMark;
        private final int pattern;
        private final boolean goalkeeper;
        private final int width;
        private final int height;

        private PlayerPortraitIcon(String name, int number, Color shirt, Color secondary,
                String teamMark, int pattern, boolean goalkeeper, int width, int height) {
            this.name = name;
            this.number = number;
            this.shirt = shirt;
            this.secondary = secondary;
            this.teamMark = teamMark;
            this.pattern = pattern;
            this.goalkeeper = goalkeeper;
            this.width = width;
            this.height = height;
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.translate(x, y);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(new GradientPaint(0, 0, new Color(47, 79, 97),
                    width, height, new Color(11, 28, 43)));
            g.fillRoundRect(0, 0, width, height, 20, 20);
            g.setColor(new Color(255, 255, 255, 22));
            g.fillOval(width / 2 - height / 3, height / 9, height * 2 / 3, height * 2 / 3);
            g.setColor(new Color(0, 0, 0, 70));
            g.fillOval(width / 2 - width / 4, height - height / 5, width / 2, height / 14);

            int head = Math.max(21, Math.min(42, height / 4));
            int headX = width / 2 - head / 2;
            int headY = height / 5;
            int torsoTop = headY + head - 2;
            int torsoHeight = height - torsoTop - height / 9;
            Color skin = new Color(199, 145, 111);
            g.setColor(new Color(30, 40, 52));
            g.fillRoundRect(width / 2 - head / 2 - 2, headY - head / 7,
                    head + 4, head / 2, head / 2, head / 2);
            g.setPaint(new GradientPaint(headX, headY, new Color(238, 188, 151),
                    headX + head, headY + head, skin.darker()));
            g.fillOval(headX, headY, head, head);
            g.setColor(new Color(42, 37, 36));
            g.fillArc(headX - 1, headY - 2, head + 2, head / 2, 0, 180);

            int shoulder = width * 3 / 5;
            int[] bodyX = {width / 2 - shoulder / 2, width / 2 - shoulder / 3, width / 2 + shoulder / 3,
                width / 2 + shoulder / 2, width / 2 + shoulder / 3, width / 2 - shoulder / 3};
            int[] bodyY = {torsoTop + head / 7, torsoTop - head / 9, torsoTop - head / 9,
                torsoTop + head / 7, torsoTop + torsoHeight, torsoTop + torsoHeight};
            g.setPaint(new GradientPaint(width / 2 - shoulder / 2, torsoTop,
                    shirt.brighter(), width / 2 + shoulder / 2, torsoTop + torsoHeight,
                    shirt.darker()));
            g.fillPolygon(bodyX, bodyY, bodyX.length);
            g.setClip(width / 2 - shoulder / 2, torsoTop, shoulder, torsoHeight);
            g.setColor(new Color(secondary.getRed(), secondary.getGreen(), secondary.getBlue(), 140));
            if (pattern == 1) {
                for (int stripe = -width; stripe < width * 2; stripe += Math.max(9, width / 9)) {
                    g.fillRect(stripe, torsoTop, Math.max(3, width / 20), torsoHeight);
                }
            } else {
                g.fillRect(width / 2 - 2, torsoTop, 4, torsoHeight);
            }
            g.setClip(null);
            g.setColor(shirt.brighter());
            g.fillRoundRect(width / 2 - shoulder / 9, torsoTop, shoulder / 4, Math.max(3, head / 8), 4, 4);
            g.setColor(new Color(255, 255, 255, 220));
            g.setFont(new Font("SansSerif", Font.BOLD, Math.max(9, width / 8)));
            if (number > 0) {
                String displayNumber = Integer.toString(number);
                FontMetrics metrics = g.getFontMetrics();
                g.drawString(displayNumber, width / 2 - metrics.stringWidth(displayNumber) / 2,
                        torsoTop + torsoHeight * 3 / 4);
            }
            if (goalkeeper) {
                g.setColor(new Color(232, 235, 120));
                g.fillRoundRect(width / 2 - shoulder / 2 - 5, torsoTop + torsoHeight / 2,
                        Math.max(7, width / 10), Math.max(8, height / 12), 5, 5);
                g.fillRoundRect(width / 2 + shoulder / 2 - 3, torsoTop + torsoHeight / 2,
                        Math.max(7, width / 10), Math.max(8, height / 12), 5, 5);
            }
            g.setColor(new Color(245, 220, 142));
            g.setFont(new Font("SansSerif", Font.BOLD, Math.max(6, width / 18)));
            FontMetrics metrics = g.getFontMetrics();
            g.drawString(teamMark, width / 2 - metrics.stringWidth(teamMark) / 2, torsoTop + torsoHeight / 3);

            g.setColor(new Color(235, 244, 247));
            g.setFont(new Font("SansSerif", Font.BOLD, Math.max(8, width / 11)));
            String label = name.length() > 22 ? name.substring(0, 20) + "…" : name;
            metrics = g.getFontMetrics();
            g.drawString(label, Math.max(4, (width - metrics.stringWidth(label)) / 2), height - 5);
            g.dispose();
        }
    }

    private final class TutorialDialog extends JDialog {
        private static final long serialVersionUID = 1L;
        private final CardLayout tutorialCards = new CardLayout();
        private final JPanel tutorialPages = new JPanel(tutorialCards);
        private final JLabel pageNumber = new JLabel("", SwingConstants.CENTER);
        private final JButton backButton = new JButton("BACK");
        private final JButton nextButton = new JButton("NEXT");
        private int page;

        private TutorialDialog() {
            super(PenaltyShootout.this, "Welcome to Penalty Shootout", true);
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            setSize(560, 390);
            setResizable(false);
            setLocationRelativeTo(PenaltyShootout.this);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    completeTutorial(false);
                }
            });

            JPanel content = new JPanel(new BorderLayout(12, 16));
            content.setBackground(NAVY);
            content.setBorder(BorderFactory.createEmptyBorder(22, 30, 20, 30));

            JLabel title = styledLabel("YOUR ROAD TO THE TOP", 25, Color.WHITE);
            title.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
            content.add(title, BorderLayout.NORTH);

            tutorialPages.setOpaque(false);
            addTutorialPage("01  •  THE CAMPAIGN",
                    "Start at Level 1. Win a challenge to unlock the next stop on the roadmap. "
                    + "Locked levels stay visible, but you can only play levels you have unlocked.");
            addTutorialPage("02  •  TAKE YOUR SHOT",
                    "Press START CHALLENGE. Click a spot inside the goal to aim and shoot, "
                    + "or use the arrow keys to aim and press SPACE. Aim carefully: shots can be saved or go wide.");
            addTutorialPage("03  •  PICK YOUR MATCH LEVEL",
                    "Choose a difficulty on the home screen. Easier settings give you more time or attempts "
                    + "and a less capable keeper; harder settings raise the challenge and speed up the keeper.");
            content.add(tutorialPages, BorderLayout.CENTER);

            JPanel footer = new JPanel(new BorderLayout(12, 8));
            footer.setOpaque(false);
            pageNumber.setForeground(new Color(172, 196, 207));
            pageNumber.setFont(new Font("SansSerif", Font.BOLD, 12));
            footer.add(pageNumber, BorderLayout.NORTH);
            JPanel buttons = new JPanel(new BorderLayout(10, 0));
            buttons.setOpaque(false);
            backButton.setForeground(Color.WHITE);
            backButton.setBackground(new Color(40, 61, 82));
            backButton.setFocusPainted(false);
            nextButton.setForeground(Color.WHITE);
            nextButton.setBackground(new Color(29, 169, 111));
            nextButton.setFocusPainted(false);
            buttons.add(backButton, BorderLayout.WEST);
            buttons.add(nextButton, BorderLayout.EAST);
            footer.add(buttons, BorderLayout.SOUTH);
            content.add(footer, BorderLayout.SOUTH);
            setContentPane(content);

            backButton.addActionListener(event -> showPage(page - 1));
            nextButton.addActionListener(event -> {
                if (page < 2) {
                    showPage(page + 1);
                } else {
                    completeTutorial(true);
                }
            });
            showPage(0);
        }

        private void addTutorialPage(String heading, String description) {
            JPanel panel = new JPanel(new BorderLayout(10, 14));
            panel.setOpaque(false);
            JLabel stepTitle = styledLabel(heading, 17, new Color(159, 240, 204));
            stepTitle.setHorizontalAlignment(SwingConstants.LEFT);
            JLabel details = new JLabel("<html><div style='width:430px;line-height:1.5'>"
                    + description + "</div></html>");
            details.setForeground(new Color(220, 237, 244));
            details.setFont(new Font("SansSerif", Font.PLAIN, 16));
            details.setVerticalAlignment(SwingConstants.TOP);
            panel.add(stepTitle, BorderLayout.NORTH);
            panel.add(details, BorderLayout.CENTER);
            tutorialPages.add(panel, Integer.toString(tutorialPages.getComponentCount()));
        }

        private void showPage(int nextPage) {
            page = Math.max(0, Math.min(2, nextPage));
            tutorialCards.show(tutorialPages, Integer.toString(page));
            pageNumber.setText("TUTORIAL  " + (page + 1) + " / 3");
            backButton.setEnabled(page > 0);
            nextButton.setText(page == 2 ? "START LEVEL 1" : "NEXT");
        }

        private void completeTutorial(boolean startLevel) {
            PREFS.putBoolean("tutorialCompleted", true);
            dispose();
            if (startLevel) openLevel(1);
        }
    }

    private final class RoadmapPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private static final int COLUMNS = 3;
        private static final int TILE_WIDTH = 86;
        private static final int TILE_HEIGHT = 54;
        private static final int ROW_SPACING = 88;
        private static final int SIDE_PADDING = 54;
        private static final int TOP_PADDING = 20;
        private int hoveredLevel;

        private RoadmapPanel() {
            setOpaque(false);
            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    int level = levelAt(event.getX(), event.getY());
                    if (level > 0 && level <= unlockedLevel) openLevel(level);
                }

                @Override
                public void mouseExited(MouseEvent event) {
                    hoveredLevel = 0;
                    setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent event) {
                    int level = levelAt(event.getX(), event.getY());
                    if (level != hoveredLevel) {
                        hoveredLevel = level;
                        setCursor(java.awt.Cursor.getPredefinedCursor(
                                level > 0 && level <= unlockedLevel
                                        ? java.awt.Cursor.HAND_CURSOR : java.awt.Cursor.DEFAULT_CURSOR));
                        repaint();
                    }
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            int rows = (MAX_LEVEL + COLUMNS - 1) / COLUMNS;
            int height = TOP_PADDING * 2 + TILE_HEIGHT + (rows - 1) * ROW_SPACING;
            return new Dimension(740, height);
        }

        private int nodeCenterX(int level) {
            int usableWidth = Math.max(TILE_WIDTH, getWidth() - SIDE_PADDING * 2);
            int index = level - 1;
            int row = index / COLUMNS;
            int column = index % COLUMNS;
            if (row % 2 == 1) column = COLUMNS - 1 - column;
            return SIDE_PADDING + TILE_WIDTH / 2
                    + column * (usableWidth - TILE_WIDTH) / (COLUMNS - 1);
        }

        private int nodeCenterY(int index) {
            return TOP_PADDING + TILE_HEIGHT / 2 + (index / COLUMNS) * ROW_SPACING;
        }

        private int levelAt(int x, int y) {
            int row = Math.round((y - TOP_PADDING - TILE_HEIGHT / 2f) / ROW_SPACING);
            if (row < 0 || row >= (MAX_LEVEL + COLUMNS - 1) / COLUMNS) return 0;
            int firstIndex = row * COLUMNS;
            for (int index = firstIndex; index < Math.min(MAX_LEVEL, firstIndex + COLUMNS); index++) {
                int level = index + 1;
                if (Math.abs(x - nodeCenterX(level)) <= TILE_WIDTH / 2
                        && Math.abs(y - nodeCenterY(index)) <= TILE_HEIGHT / 2) return level;
            }
            return 0;
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            int level = levelAt(event.getX(), event.getY());
            if (level == 0) return null;
            return level <= unlockedLevel
                    ? "Play Level " + level + " — " + challengeKind(level).toLowerCase() + " challenge"
                    : "Complete earlier levels to unlock Level " + level;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            java.awt.Rectangle clip = g.getClipBounds();
            if (clip == null) clip = new java.awt.Rectangle(0, 0, width, height);
            int rows = (MAX_LEVEL + COLUMNS - 1) / COLUMNS;
            int firstRow = Math.max(0,
                    (clip.y - TOP_PADDING - TILE_HEIGHT) / ROW_SPACING);
            int lastRow = Math.min(rows - 1,
                    (clip.y + clip.height - TOP_PADDING) / ROW_SPACING + 1);

            g.setPaint(new GradientPaint(0, 0, new Color(13, 45, 47, 180),
                    0, height, new Color(12, 30, 43, 120)));
            g.fillRect(0, 0, width, height);
            for (int row = firstRow; row <= lastRow; row++) {
                int y = TOP_PADDING + TILE_HEIGHT / 2 + row * ROW_SPACING;
                g.setColor(new Color(192, 236, 218, 12));
                g.drawLine(18, y, width - 18, y);
            }

            g.setStroke(new BasicStroke(13, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(14, 28, 38, 230));
            int firstEdge = Math.max(0, firstRow * COLUMNS - 1);
            int lastEdge = Math.min(MAX_LEVEL - 2, (lastRow + 1) * COLUMNS - 1);
            for (int index = firstEdge; index <= lastEdge; index++) {
                g.drawLine(nodeCenterX(index + 1), nodeCenterY(index),
                        nodeCenterX(index + 2), nodeCenterY(index + 1));
            }

            g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    10, new float[]{7, 8}, 0));
            g.setColor(new Color(210, 230, 224, 135));
            for (int index = firstEdge; index <= lastEdge; index++) {
                g.drawLine(nodeCenterX(index + 1), nodeCenterY(index),
                        nodeCenterX(index + 2), nodeCenterY(index + 1));
            }

            g.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(42, 194, 129, 210));
            for (int index = firstEdge; index <= Math.min(lastEdge, highestCompletedLevel - 1); index++) {
                g.drawLine(nodeCenterX(index + 1), nodeCenterY(index),
                        nodeCenterX(index + 2), nodeCenterY(index + 1));
            }

            for (int row = firstRow; row <= lastRow; row++) {
                int firstIndex = row * COLUMNS;
                for (int index = firstIndex; index < Math.min(MAX_LEVEL, firstIndex + COLUMNS); index++) {
                    int level = index + 1;
                    int centerX = nodeCenterX(level);
                    int centerY = nodeCenterY(index);
                    boolean completed = level <= highestCompletedLevel;
                    boolean unlocked = level <= unlockedLevel;
                    boolean highlighted = level == selectedLevel || level == hoveredLevel;
                    if (level == unlockedLevel) {
                        int glow = (int) (5 + 4 * Math.sin(System.currentTimeMillis() / 280.0));
                        g.setColor(new Color(53, 215, 150, 48));
                        g.fillOval(centerX - TILE_WIDTH / 2 - glow, centerY - TILE_HEIGHT / 2 - glow,
                                TILE_WIDTH + glow * 2, TILE_HEIGHT + glow * 2);
                    }
                    Color fill = completed ? new Color(23, 105, 78)
                            : unlocked ? new Color(30, 102, 82)
                            : new Color(27, 43, 57);
                    g.setColor(fill);
                    g.fillRoundRect(centerX - TILE_WIDTH / 2, centerY - TILE_HEIGHT / 2,
                            TILE_WIDTH, TILE_HEIGHT, 18, 18);
                    g.setColor(highlighted ? new Color(136, 255, 195)
                            : unlocked ? new Color(84, 193, 146, 210)
                            : new Color(93, 121, 136, 170));
                    g.setStroke(new BasicStroke(highlighted ? 2.5f : 1.2f));
                    g.drawRoundRect(centerX - TILE_WIDTH / 2, centerY - TILE_HEIGHT / 2,
                            TILE_WIDTH, TILE_HEIGHT, 18, 18);
                    g.setFont(new Font("SansSerif", Font.BOLD, 19));
                    g.setColor(unlocked ? Color.WHITE : new Color(154, 171, 181));
                    String number = Integer.toString(level);
                    FontMetrics metrics = g.getFontMetrics();
                    g.drawString(number, centerX - metrics.stringWidth(number) / 2, centerY - 2);
                    g.setFont(new Font("SansSerif", Font.BOLD, 8));
                    g.setColor(unlocked ? new Color(188, 232, 207) : new Color(134, 153, 163));
                    String mode = completed ? "COMPLETE" : unlocked
                            ? challengeKind(level) : "LOCKED";
                    metrics = g.getFontMetrics();
                    g.drawString(mode, centerX - metrics.stringWidth(mode) / 2, centerY + 15);
                }
            }
            g.dispose();
        }
    }

    private void animateScene() {
        if (!isDisplayable()) return;
        if (levelGrid.isShowing() && unlockedLevel <= MAX_LEVEL) levelGrid.repaint();
        long now = System.currentTimeMillis();
        if (keeperDiving) {
            long diveElapsed = now - keeperDiveStartedAt;
            if (!shotInProgress && diveElapsed > 700) {
                diveFrame = (int) Math.max(0, (1050 - diveElapsed) * 100 / 350);
                double recovery = Math.min(1, (diveElapsed - 700) / 350.0);
                keeperX = keeperTargetX + (.5 - keeperTargetX) * recovery;
            } else {
                diveFrame = (int) Math.min(100, diveElapsed * 100 / keeperDiveDuration());
            }
            if (diveElapsed >= 1050) {
                keeperDiving = false;
                diveFrame = 0;
            }
        }
        if (keeperHasSecuredBall && keeperDiving) {
            ballX = field.keeperCatchX(keeperX, diveDirection);
            ballY = field.keeperCatchY();
        }
        if (!shotInProgress && !keeperDiving) {
            keeperX = .5 + .12 * Math.sin(now / 850.0);
            if (now >= nextPatrolAt) {
                keeperZone = keeperX < .46 ? 0 : keeperX > .54 ? 2 : 1;
                nextPatrolAt = now + 250;
            }
        }
        field.repaint();
    }

    private final class Challenge {
        private final String type;
        private final int target;
        private final int seconds;
        private final int shotsAllowed;
        private final int[] sequence;

        private Challenge(String type, int target, int seconds, int shotsAllowed, int[] sequence) {
            this.type = type;
            this.target = target;
            this.seconds = seconds;
            this.shotsAllowed = shotsAllowed;
            this.sequence = sequence;
        }
    }

    private final class FieldPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private boolean aimVisible = true;
        private int result;

        private FieldPanel() {
            setOpaque(false);
            setFocusable(true);
        }

        private int goalTop() { return 34; }
        private int goalHeight() { return Math.min(190, getHeight() / 2); }
        private int netTop() { return goalTop() + 5; }
        private int netHeight() { return goalHeight() - 10; }
        private double netLeft() { return .16; }
        private double netRight() { return .84; }
        private int goalX() { return (int) (getWidth() * netLeft()); }
        private int goalWidth() { return (int) (getWidth() * (netRight() - netLeft())); }
        private int pitchLeftAt(double depth) {
            return (int) (getWidth() * (.37 - .34 * depth));
        }
        private int pitchRightAt(double depth) {
            return (int) (getWidth() * (.63 + .34 * depth));
        }
        private int pitchX(double across, double depth) {
            return (int) (pitchLeftAt(depth)
                    + (pitchRightAt(depth) - pitchLeftAt(depth)) * across);
        }
        private int pitchY(double depth, int top) {
            return top + (int) ((getHeight() - top) * depth);
        }
        private double keeperCatchX(double center, int direction) {
            double scale = Math.max(.7, Math.min(1.15, getWidth() / 720.0));
            double pullIn = keeperHasSecuredBall ? 1
                    : Math.min(1, Math.max(0, (diveFrame - 58) / 42.0));
            return Math.max(netLeft() + .025, Math.min(netRight() - .025,
                    center + direction * 38 * scale * (1 - pullIn) / Math.max(1, getWidth())));
        }
        private double keeperCatchY() {
            double scale = Math.max(.7, Math.min(1.15, getWidth() / 720.0));
            double progress = diveFrame / 100.0;
            int lift = (int) (Math.sin(Math.PI * progress) * 38);
            double pullIn = keeperHasSecuredBall ? 1
                    : Math.min(1, Math.max(0, (diveFrame - 58) / 42.0));
            int armY = (int) (-123 * (1 - pullIn) - 74 * pullIn);
            int gloveY = goalTop() + goalHeight() - 4 - lift + (int) (armY * scale);
            return Math.max(.04, Math.min(.9, gloveY / (double) Math.max(1, getHeight())));
        }
        private void setAimVisible(boolean visible) { aimVisible = visible; }
        private void setResult(int value) { result = value; }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            int quality = Math.max(0, Math.min(2, graphicsSelector.getSelectedIndex()));
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    quality == 0 ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();

            g.setPaint(new GradientPaint(0, 0, new Color(33, 131, 83), 0, height, new Color(12, 76, 52)));
            g.fillRoundRect(0, 0, width, height, 22, 22);
            int goalBottom = goalTop() + goalHeight();
            int stadiumIndex = selectedClubIndex();
            Color stadiumColor = CLUB_COLORS[stadiumIndex];
            g.setPaint(new GradientPaint(0, 0, new Color(10, 22, 37),
                    0, goalBottom, new Color(stadiumColor.getRed(), stadiumColor.getGreen(),
                            stadiumColor.getBlue(), 130)));
            g.fillRect(0, 0, width, goalBottom);
            g.setColor(new Color(6, 17, 29, 210));
            g.fillRect(0, 0, width, Math.max(18, goalTop() / 2));
            Color stadiumSecondary = CLUB_SECONDARY_COLORS[stadiumIndex];
            g.setColor(new Color(stadiumColor.getRed(), stadiumColor.getGreen(),
                    stadiumColor.getBlue(), 175));
            g.fillRect(0, goalTop() / 2 - 3, width, 5);
            g.setColor(new Color(stadiumSecondary.getRed(), stadiumSecondary.getGreen(),
                    stadiumSecondary.getBlue(), 105));
            g.fillRect(0, goalTop() - 5, width, 4);
            int crowdStride = quality == 0 ? 18 : quality == 1 ? 12 : 7;
            Random crowd = new Random(stadiumIndex * 37L + 11);
            for (int y = goalTop() / 2; y < goalBottom; y += quality == 2 ? 8 : 13) {
                for (int x = 6; x < width - 6; x += crowdStride) {
                    if (x > width * .17 && x < width * .83 && y > goalTop() - 4) continue;
                    int tint = crowd.nextInt(65);
                    g.setColor(new Color(Math.min(255, 174 + tint),
                            Math.min(255, 183 + tint), Math.min(255, 188 + tint), 175));
                    g.fillOval(x, y, quality == 2 ? 3 : 2, quality == 2 ? 4 : 3);
                }
            }
            g.setColor(new Color(245, 250, 255, quality == 2 ? 185 : 115));
            g.setStroke(new BasicStroke(quality == 0 ? 2 : 3));
            g.drawLine(0, goalTop() / 2, width / 5, 2);
            g.drawLine(width, goalTop() / 2, width * 4 / 5, 2);
            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            g.setColor(new Color(255, 255, 255, 210));
            String venue = stadiumName().toUpperCase();
            g.drawString(venue, Math.max(8, (width - g.getFontMetrics().stringWidth(venue)) / 2), 15);
            int pitchTop = goalTop() + goalHeight() + 4;
            int[] pitchX = {width * 37 / 100, width * 63 / 100, width * 97 / 100, width * 3 / 100};
            int[] pitchY = {pitchTop, pitchTop, height, height};
            g.setPaint(new GradientPaint(0, pitchTop, new Color(47, 139, 78),
                    0, height, new Color(8, 75, 44)));
            g.fillRect(0, pitchTop, width, height - pitchTop);
            int pitchDepth = Math.max(1, height - pitchTop);
            for (int band = 0; band < 8; band++) {
                int y = pitchTop + pitchDepth * band / 8;
                g.setColor(band % 2 == 0 ? new Color(206, 255, 208, 20)
                        : new Color(0, 29, 18, 19));
                g.fillRect(0, y, width, Math.max(1, pitchDepth / 8));
            }
            Graphics2D pitch = (Graphics2D) g.create();
            pitch.setClip(new java.awt.Polygon(pitchX, pitchY, 4));
            float lineWidth = quality == 0 ? 1.4f : 2.1f;
            pitch.setColor(new Color(244, 255, 247, quality == 0 ? 205 : 235));
            pitch.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Perspective-projected touchlines, goal line, and penalty-area markings.
            pitch.drawLine(pitchX(0, 0), pitchY(0, pitchTop), pitchX(0, 1), pitchY(1, pitchTop));
            pitch.drawLine(pitchX(1, 0), pitchY(0, pitchTop), pitchX(1, 1), pitchY(1, pitchTop));
            pitch.drawLine(pitchX(0, 0), pitchY(0, pitchTop), pitchX(1, 0), pitchY(0, pitchTop));

            double boxLeft = .204;
            double boxRight = 1 - boxLeft;
            double boxDepth = .39;
            pitch.drawLine(pitchX(boxLeft, 0), pitchY(0, pitchTop),
                    pitchX(boxLeft, boxDepth), pitchY(boxDepth, pitchTop));
            pitch.drawLine(pitchX(boxRight, 0), pitchY(0, pitchTop),
                    pitchX(boxRight, boxDepth), pitchY(boxDepth, pitchTop));
            pitch.drawLine(pitchX(boxLeft, boxDepth), pitchY(boxDepth, pitchTop),
                    pitchX(boxRight, boxDepth), pitchY(boxDepth, pitchTop));

            double goalAreaLeft = .433;
            double goalAreaRight = 1 - goalAreaLeft;
            double goalAreaDepth = .14;
            pitch.drawLine(pitchX(goalAreaLeft, 0), pitchY(0, pitchTop),
                    pitchX(goalAreaLeft, goalAreaDepth), pitchY(goalAreaDepth, pitchTop));
            pitch.drawLine(pitchX(goalAreaRight, 0), pitchY(0, pitchTop),
                    pitchX(goalAreaRight, goalAreaDepth), pitchY(goalAreaDepth, pitchTop));
            pitch.drawLine(pitchX(goalAreaLeft, goalAreaDepth), pitchY(goalAreaDepth, pitchTop),
                    pitchX(goalAreaRight, goalAreaDepth), pitchY(goalAreaDepth, pitchTop));

            double spotDepth = .245;
            int spotX = pitchX(.5, spotDepth);
            int spotY = pitchY(spotDepth, pitchTop);
            pitch.fillOval(spotX - 3, spotY - 3, 6, 6);

            // The penalty arc is only visible beyond the outer edge of the box.
            int arcWidth = (int) ((pitchRightAt(spotDepth) - pitchLeftAt(spotDepth)) * .27);
            int arcHeight = Math.max(12, (int) (pitchDepth * .40));
            int arcX = spotX - arcWidth / 2;
            int arcY = spotY - arcHeight / 2;
            Shape oldPitchClip = pitch.getClip();
            pitch.clipRect(0, pitchY(boxDepth, pitchTop), width, height - pitchY(boxDepth, pitchTop));
            pitch.drawArc(arcX, arcY, arcWidth, arcHeight, 180, 180);
            pitch.setClip(oldPitchClip);
            pitch.dispose();

            int gx = goalX();
            int gy = goalTop();
            int gw = goalWidth();
            int gh = goalHeight();
            g.setColor(new Color(255, 255, 255, 42));
            g.fillRect(gx + 5, gy + 5, gw - 10, gh - 10);
            g.setColor(new Color(242, 247, 244));
            g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawRect(gx, gy, gw, gh);
            g.setStroke(new BasicStroke(1));
            g.setColor(new Color(255, 255, 255, 105));
            for (int x = gx + 20; x < gx + gw; x += quality == 2 ? 18 : 26) g.drawLine(x, gy + 4, x, gy + gh);
            for (int y = gy + 20; y < gy + gh; y += quality == 2 ? 18 : 26) g.drawLine(gx + 4, y, gx + gw, y);
            int keeperLift = keeperDiving ? (int) (Math.sin(Math.PI * diveFrame / 100.0) * 38) : 0;
            paintPlayer(g, (int) (keeperX * width), gy + gh - 4 - keeperLift, true);
            paintPlayer(g, width / 2, height - 18, false);

            if (shotInProgress || keeperCaughtBall && keeperDiving) {
                int bx = (int) (ballX * width);
                int by = (int) (ballY * height);
                int diameter = keeperCaughtBall ? 25 : 12 + (int) ((1 - ballY) * 13);
                paintBall(g, bx, by, diameter, keeperCaughtBall ? diveFrame * .035 : diveFrame * .12);
            }
            if (aimVisible && active) {
                int ax = (int) (aimX * width);
                int ay = gy + (int) (aimY * gh);
                g.setColor(new Color(255, 255, 255, 210));
                g.setStroke(new BasicStroke(2));
                g.drawOval(ax - 11, ay - 11, 22, 22);
                g.drawLine(ax - 16, ay, ax + 16, ay);
                g.drawLine(ax, ay - 16, ax, ay + 16);
            }
            if (result != 0) {
                String text = result > 0 ? "GOAL!"
                        : result == -1 ? "SAVED!"
                        : result == -3 ? "WRONG ZONE!" : "WIDE!";
                g.setFont(new Font("SansSerif", Font.BOLD, 20));
                g.setColor(Color.WHITE);
                int textWidth = g.getFontMetrics().stringWidth(text);
                g.drawString(text, (width - textWidth) / 2, height - 12);
            }
            g.dispose();
        }

        private void paintBall(Graphics2D g, int centerX, int centerY, int diameter, double rotation) {
            Graphics2D ball = (Graphics2D) g.create();
            ball.translate(centerX, centerY);
            ball.rotate(rotation);
            double radius = diameter / 2.0;
            ball.setColor(new Color(0, 0, 0, 90));
            ball.fill(new Ellipse2D.Double(-radius + 3, -radius + diameter * .62,
                    diameter, Math.max(4, diameter * .38)));
            ball.setPaint(new RadialGradientPaint(
                    new Point2D.Double(-radius * .35, -radius * .42),
                    (float) (radius * 1.8),
                    new float[]{0f, .55f, 1f},
                    new Color[]{Color.WHITE, new Color(230, 236, 239), new Color(111, 127, 143)}));
            ball.fill(new Ellipse2D.Double(-radius, -radius, diameter, diameter));
            ball.setColor(new Color(31, 43, 57, 210));
            ball.setStroke(new BasicStroke(Math.max(1f, diameter / 18f)));
            int patchSize = Math.max(3, diameter / 4);
            int[][] patchCenters = {
                {-diameter / 5, -diameter / 5},
                {diameter / 3, -diameter / 7},
                {diameter / 5, diameter / 3},
                {-diameter / 3, diameter / 4}
            };
            for (int[] patch : patchCenters) {
                int x = patch[0];
                int y = patch[1];
                int half = patchSize / 2;
                int[] px = {x - half / 2, x + half / 2, x + half, x + half / 2, x - half / 2, x - half};
                int[] py = {y - half, y - half, y, y + half, y + half, y};
                ball.fillPolygon(px, py, px.length);
            }
            ball.setColor(new Color(255, 255, 255, 205));
            ball.fill(new Ellipse2D.Double(-radius * .58, -radius * .62, radius * .52, radius * .4));
            ball.setColor(new Color(43, 58, 73, 170));
            ball.draw(new Ellipse2D.Double(-radius, -radius, diameter, diameter));
            ball.dispose();
        }

        private void paintPlayer(Graphics2D g, int centerX, int baseline, boolean goalkeeper) {
            Graphics2D player = (Graphics2D) g.create();
            double scale = Math.max(.7, Math.min(1.15, getWidth() / 720.0));
            player.translate(centerX, baseline);
            player.scale(scale, scale);
            boolean diving = goalkeeper && keeperDiving;
            double diveProgress = diveFrame / 100.0;
            double lean = diving ? diveDirection * 62 * Math.min(1, diveProgress * 1.7) : 0;
            player.rotate(Math.toRadians(lean));
            player.setColor(new Color(0, 0, 0, 65));
            player.fill(new Ellipse2D.Double(-25, -2, 50, 9));
            Color shirt = goalkeeper ? keeperColorForLevel(selectedLevel) : selectedShirtColor();
            Color shirtSecondary = goalkeeper ? Color.WHITE : selectedSecondaryShirtColor();
            Color shirtLight = shirt.brighter();
            Color skin = new Color(203, 143, 106);
            int bodyTop = goalkeeper ? -98 : -70;
            int bodyHeight = goalkeeper ? 42 : 34;

            player.setColor(skin.darker());
            if (diving) {
                paintDivingLegs(player, bodyTop + bodyHeight - 1, diveProgress, skin);
            } else {
                player.fillRoundRect(-15, bodyTop + bodyHeight - 1, 12, 42, 8, 8);
                player.fillRoundRect(3, bodyTop + bodyHeight - 1, 12, 42, 8, 8);
                player.setColor(new Color(20, 34, 56));
                player.fillRoundRect(-19, bodyTop + bodyHeight + 35, 22, 8, 5, 5);
                player.fillRoundRect(1, bodyTop + bodyHeight + 35, 22, 8, 5, 5);
            }

            player.setPaint(new GradientPaint(-16, bodyTop, shirtLight, 18, bodyTop + bodyHeight, shirt.darker()));
            player.fillRoundRect(-19, bodyTop, 38, bodyHeight, 12, 12);
            player.setColor(new Color(shirtSecondary.getRed(), shirtSecondary.getGreen(),
                    shirtSecondary.getBlue(), 100));
            if (!goalkeeper && selectedPlayerKit().pattern == 1) {
                player.fillRoundRect(-5, bodyTop + 2, 7, bodyHeight - 4, 4, 4);
                player.fillRoundRect(10, bodyTop + 2, 5, bodyHeight - 4, 4, 4);
            } else {
                player.fillRoundRect(-12, bodyTop + 5, 5, bodyHeight - 12, 4, 4);
            }
            player.setColor(shirtSecondary.brighter());
            player.fillRoundRect(-7, bodyTop, 14, 5, 5, 5);
            String shirtName = goalkeeper ? keeperNameForLevel(selectedLevel) : selectedPlayerName();
            String nameText = shortShirtName(shirtName);
            player.setColor(new Color(255, 255, 255, 225));
            player.setFont(new Font("SansSerif", Font.BOLD, goalkeeper ? 5 : 6));
            FontMetrics shirtMetrics = player.getFontMetrics();
            player.drawString(nameText, -shirtMetrics.stringWidth(nameText) / 2, bodyTop + 15);
            String numberText = goalkeeper
                    ? Integer.toString(keeperNumberForLevel(selectedLevel)) : selectedPlayerNumber();
            player.setFont(new Font("SansSerif", Font.BOLD, goalkeeper ? 8 : 10));
            shirtMetrics = player.getFontMetrics();
            player.drawString(numberText, -shirtMetrics.stringWidth(numberText) / 2, bodyTop + 29);
            player.setColor(new Color(255, 255, 255, 190));
            player.drawOval(11, bodyTop + 6, 7, 7);
            player.setFont(new Font("SansSerif", Font.BOLD, 3));
            String mark = goalkeeper ? initials(keeperNationForLevel(selectedLevel))
                    : CUSTOM_PLAYER.equals(playerSelector.getSelectedItem())
                            ? "MY" : selectedPlayerKit().monogram;
            shirtMetrics = player.getFontMetrics();
            player.drawString(mark, 14 - shirtMetrics.stringWidth(mark) / 2, bodyTop + 11);
            paintArms(player, goalkeeper, diving, diveProgress, bodyTop, shirt, skin);
            player.setColor(skin);
            player.fillRoundRect(-6, bodyTop - 10, 12, 15, 5, 5);
            player.setPaint(new GradientPaint(-9, bodyTop - 36, new Color(239, 190, 153),
                    9, bodyTop - 12, skin.darker()));
            player.fillOval(-13, bodyTop - 39, 26, 29);
            player.setColor(new Color(48, 39, 39));
            player.fillArc(-14, bodyTop - 41, 28, 19, 0, 180);
            player.setColor(new Color(38, 43, 51));
            player.fillOval(-5, bodyTop - 27, 2, 2);
            player.fillOval(4, bodyTop - 27, 2, 2);
            player.dispose();
        }

        private String shortShirtName(String name) {
            String[] parts = name.trim().split("\\s+");
            String surname = parts[parts.length - 1].toUpperCase();
            return surname.length() > 8 ? surname.substring(0, 8) : surname;
        }

        private void paintArms(Graphics2D player, boolean goalkeeper, boolean diving,
                double diveProgress, int bodyTop, Color shirt, Color skin) {
            player.setStroke(new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            player.setColor(new Color(27, 42, 65));
            if (diving) {
                double reach = Math.min(1, diveProgress * 1.8);
                double gather = keeperHasSecuredBall ? 1
                        : Math.min(1, Math.max(0, (diveProgress - .58) / .42));
                int reachingGloveX = diveDirection < 0 ? -47 : diveDirection > 0 ? 7 : -32;
                int otherGloveX = diveDirection > 0 ? 47 : diveDirection < 0 ? -7 : 32;
                int gloveY = bodyTop - 25;
                int reachingX = (int) (-12 + (reachingGloveX + 12) * reach);
                int otherX = (int) (12 + (otherGloveX - 12) * reach);
                int handY = bodyTop + 8 + (int) ((gloveY - bodyTop - 8) * reach);
                int leftHandX = (int) (reachingX + ((-11) - reachingX) * gather);
                int rightHandX = (int) (otherX + 11 + ((11) - (otherX + 11)) * gather);
                int gatheredY = bodyTop + 24;
                int leftHandY = handY + (int) ((gatheredY - handY) * gather);
                int rightHandY = handY + (diveDirection == 0 ? 0 : 5)
                        + (int) ((gatheredY - handY) * gather);
                player.drawLine(-12, bodyTop + 8, leftHandX, leftHandY);
                player.drawLine(12, bodyTop + 8, rightHandX, rightHandY);
                player.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                player.setColor(skin);
                player.drawLine(-12, bodyTop + 8, leftHandX, leftHandY);
                player.drawLine(12, bodyTop + 8, rightHandX, rightHandY);
                if (goalkeeper) {
                    player.setColor(new Color(29, 45, 61));
                    player.fillRoundRect(leftHandX - 9, leftHandY - 7, 19, 15, 7, 7);
                    player.fillRoundRect(rightHandX - 9, rightHandY - 7, 19, 15, 7, 7);
                    player.setColor(new Color(225, 247, 105));
                    player.fillRoundRect(leftHandX - 7, leftHandY - 6, 15, 12, 6, 6);
                    player.fillRoundRect(rightHandX - 7, rightHandY - 6, 15, 12, 6, 6);
                    player.setColor(new Color(255, 255, 255, 155));
                    player.drawLine(leftHandX - 3, leftHandY - 3, leftHandX + 2, leftHandY - 3);
                    player.drawLine(rightHandX - 3, rightHandY - 3, rightHandX + 2, rightHandY - 3);
                }
                return;
            }

            player.drawLine(-12, bodyTop + 9, -25, bodyTop + 17);
            player.drawLine(12, bodyTop + 9, 25, bodyTop + 17);
            player.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            player.setColor(skin);
            player.drawLine(-12, bodyTop + 10, -25, bodyTop + 17);
            player.drawLine(12, bodyTop + 10, 25, bodyTop + 17);
            if (goalkeeper) {
                player.setColor(new Color(225, 247, 105));
                player.fillRoundRect(-33, bodyTop + 11, 15, 12, 5, 5);
                player.fillRoundRect(18, bodyTop + 11, 15, 12, 5, 5);
            }
        }

        private void paintDivingLegs(Graphics2D player, int hipY, double progress, Color skin) {
            double kick = Math.sin(Math.PI * Math.min(1, progress * 1.35));
            int leftKneeX = (int) (-11 - diveDirection * 13 * kick);
            int rightKneeX = (int) (10 - diveDirection * 8 * kick);
            int kneeY = hipY + 12 - (int) (kick * 8);
            int leftFootX = (int) (-16 - diveDirection * 34 * kick);
            int rightFootX = (int) (15 - diveDirection * 22 * kick);
            int footY = hipY + 36 - (int) (kick * 15);
            player.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            player.setColor(new Color(23, 39, 59));
            player.drawLine(-8, hipY, leftKneeX, kneeY);
            player.drawLine(8, hipY, rightKneeX, kneeY + 2);
            player.drawLine(leftKneeX, kneeY, leftFootX, footY);
            player.drawLine(rightKneeX, kneeY + 2, rightFootX, footY + 3);
            player.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            player.setColor(skin);
            player.drawLine(-8, hipY, leftKneeX, kneeY);
            player.drawLine(8, hipY, rightKneeX, kneeY + 2);
            player.drawLine(leftKneeX, kneeY, leftFootX, footY);
            player.drawLine(rightKneeX, kneeY + 2, rightFootX, footY + 3);
            player.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            player.setColor(new Color(20, 34, 56));
            player.drawLine(leftFootX - 6, footY, leftFootX + 9, footY);
            player.drawLine(rightFootX - 6, footY + 3, rightFootX + 9, footY + 3);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PenaltyShootout().setVisible(true));
    }
}
