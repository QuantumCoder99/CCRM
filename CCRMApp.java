public class CCRMApp {
    public static void main(String[] args) {
        boolean runCli = args != null && args.length > 0 && "--cli".equalsIgnoreCase(args[0]);
        if (runCli) {
            CCRMSystem system = new CCRMSystem();
            system.run();
            return;
        }
        javax.swing.SwingUtilities.invokeLater(() -> {
            CCRMGui gui = new CCRMGui();
            gui.setVisible(true);
        });
    }
} 