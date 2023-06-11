package upe.binding.lanterna;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import upe.process.messages.UProcessMessage;

public class LanternaApplicationWindow {
    private UpeLanternaPanel activePanel = null;
    private Window activeWindow = null;
    private Terminal terminal;
    private Screen screen;
    private MultiWindowTextGUI gui;

    public void init() throws Exception {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
    }

    public void activatePanel(UpeLanternaPanel panel) {
        if( this.activePanel != null ) {
            this.activePanel.unbindFromProcess();
        }
        this.activePanel = panel;
        // Create gui and start gui
        if( activeWindow != null ) {
            gui.removeWindow(this.activeWindow);
        }
        this.activePanel = panel;
        this.activeWindow = new BasicWindow();
        this.activeWindow.setComponent(this.activePanel);
        gui.addWindowAndWait(this.activeWindow);
    }

    public void showSystemMessage(UProcessMessage msg) {
        new MessageDialogBuilder()
                .setTitle("Message "+msg.getMessageID())
                .setText(msg.getMessageText())
                .build()
                .showDialog(this.gui);
    }
}
