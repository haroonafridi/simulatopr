package com.hkcapital.portoflio.treemenu;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class SwingSideMenuWithJTree extends JFrame {
    private final JTree navTree;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);

    public SwingSideMenuWithJTree() {
        super("Swing Side Menu with JTree");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ----- Left: Navigation JTree -----
        DefaultMutableTreeNode root = buildTreeModel();
        navTree = new JTree(root);
        navTree.setRootVisible(false);
        navTree.setShowsRootHandles(true);
        navTree.setRowHeight(24);
        navTree.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Optional: nicer icons via UIManager defaults (works on most LAFs)
        DefaultTreeCellRenderer r = new DefaultTreeCellRenderer();
        r.setLeafIcon(UIManager.getIcon("FileView.fileIcon"));
        r.setClosedIcon(UIManager.getIcon("FileView.directoryIcon"));
        r.setOpenIcon(UIManager.getIcon("FileView.directoryIcon"));
        navTree.setCellRenderer(r);

        // Expand all top-level nodes initially


        // Selection -> switch card
        navTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override public void valueChanged(TreeSelectionEvent e) {
                Object nodeObj = navTree.getLastSelectedPathComponent();
                if (!(nodeObj instanceof DefaultMutableTreeNode)) return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeObj;
                Object uo = node.getUserObject();
                if (uo == null) return;
                String key = uo.toString();
                cardLayout.show(content, key);
            }
        });

        // Double-click to toggle expand/collapse on non-leaf nodes
        navTree.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = navTree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        if (!node.isLeaf()) {
                            if (navTree.isExpanded(path)) navTree.collapsePath(path); else navTree.expandPath(path);
                        }
                    }
                }
                // Simple context menu (right-click)
                if (SwingUtilities.isRightMouseButton(e)) {
                    TreePath path = navTree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        navTree.setSelectionPath(path);
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem refresh = new JMenuItem("Refresh");

                        menu.add(refresh);
                        menu.show(navTree, e.getX(), e.getY());
                    }
                }
            }
        });

        JScrollPane left = new JScrollPane(navTree);
        left.setPreferredSize(new Dimension(240, 480));
        add(left, BorderLayout.WEST);

        // ----- Right: Content area with CardLayout -----
        addCards();
        add(content, BorderLayout.CENTER);

        // Status bar (optional)
        JLabel status = new JLabel(" Ready");
        status.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230,230,230)));
        add(status, BorderLayout.SOUTH);

        setSize(960, 640);
        setLocationRelativeTo(null);
    }

    private DefaultMutableTreeNode buildTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");

        DefaultMutableTreeNode dashboard = new DefaultMutableTreeNode("Dashboard");
        DefaultMutableTreeNode trading = new DefaultMutableTreeNode("Trading");
        DefaultMutableTreeNode analytics = new DefaultMutableTreeNode("Analytics");
        DefaultMutableTreeNode settings = new DefaultMutableTreeNode("Settings");

        // Trading children as example sections
        trading.add(new DefaultMutableTreeNode("Gold"));
        trading.add(new DefaultMutableTreeNode("Nasdaq"));
        trading.add(new DefaultMutableTreeNode("Nikkei"));
        trading.add(new DefaultMutableTreeNode("Crude Oil"));

        analytics.add(new DefaultMutableTreeNode("Performance"));
        analytics.add(new DefaultMutableTreeNode("Risk"));
        analytics.add(new DefaultMutableTreeNode("Sentiment"));

        // Assemble
        root.add(dashboard);
        root.add(trading);
        root.add(analytics);
        root.add(settings);

        return root;
    }

    private void addCards() {
        // Helper to create a simple placeholder panel per "route"
        addCard("Dashboard", placeholder("Dashboard"));
        addCard("Trading", placeholder("Trading"));
        addCard("Gold", placeholder("Gold"));
        addCard("Nasdaq", placeholder("Nasdaq"));
        addCard("Nikkei", placeholder("Nikkei"));
        addCard("Crude Oil", placeholder("Crude Oil"));
        addCard("Analytics", placeholder("Analytics"));
        addCard("Performance", placeholder("Performance"));
        addCard("Risk", placeholder("Risk"));
        addCard("Sentiment", placeholder("Sentiment"));
        addCard("Settings", placeholder("Settings"));

        // Default visible card
        cardLayout.show(content, "Dashboard");
    }

    private JPanel placeholder(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel heading = new JLabel(title);
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 24f));

        JTextArea info = new JTextArea("This is the '" + title + "' view.\n\nReplace this panel with your real content.\n\nTip: Use CardLayout for lightweight routing.");
        info.setEditable(false);
        info.setOpaque(false);
        info.setFont(info.getFont().deriveFont(14f));

        p.add(heading, BorderLayout.NORTH);
        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private void addCard(String key, JComponent comp) {
        content.add(comp, key);
    }



    public static void main(String[] args) {
// Ensure a modern LAF (optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}


        SwingUtilities.invokeLater(() -> {
            SwingSideMenuWithJTree app = new SwingSideMenuWithJTree();
            app.setVisible(true);
        });
}
}
