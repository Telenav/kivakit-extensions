package com.telenav.kivakit.logs.client.view.panels.session;

import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.logs.client.view.ClientLogPanel;
import com.telenav.kivakit.logs.server.session.Session;
import com.telenav.kivakit.logs.server.session.SessionStore;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.layout.HorizontalBoxLayout;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.telenav.kivakit.ui.desktop.layout.Spacing.MANUAL_SPACING;

/**
 * @author jonathanl (shibo)
 */
public class SessionPanel extends KivaKitPanel
{
    /** The parent panel */
    private final ClientLogPanel parent;

    /** The sessions to choose from */
    private JComboBox<Session> sessionDropDown;

    public SessionPanel(final ClientLogPanel parent)
    {
        this.parent = parent;

        new HorizontalBoxLayout(this, MANUAL_SPACING, 24)
                .add(sessionDropDown())
                .add(deleteSessionButton());
    }

    public Session currentSession()
    {
        return (Session) sessionDropDown.getSelectedItem();
    }

    public void loadSessions()
    {
        SessionStore.get().load();
        updateSessionDropDown();
    }

    public void newSession(final Session session)
    {
        SessionStore.get().add(session);
        parent.tablePanel().clear();
        updateSessionDropDown();
        sessionDropDown().setSelectedItem(session);
    }

    private JButton deleteSessionButton()
    {
        final var button = KivaKitTheme.get().newButton("Delete Session");
        button.addActionListener(event ->
        {
            // Get the selected session
            final var session = (Session) sessionDropDown().getSelectedItem();
            if (session != null)
            {
                // and its index
                final var index = sessionDropDown().getSelectedIndex();

                // and if the session is live
                if (parent.connector().isConnected())
                {
                    // disconnect it and clear the log panel
                    new KivaKitThread("Disconnecter", () -> parent.connector().disconnect()).start();
                    parent.clear();
                }
                else
                {
                    // otherwise, delete the session from storage
                    SessionStore.get().delete(session);
                }

                // Then, update the session dropdown
                updateSessionDropDown();

                // and select the nearest item
                final var count = sessionDropDown().getItemCount();
                if (count > 0)
                {
                    sessionDropDown().setSelectedIndex(Math.min(count - 1, index));
                }

                // and if there is nothing in the selected log,
                if (count == 0)
                {
                    // then clear the log panel
                    parent.clear();
                }
            }
        });
        return button;
    }

    private JComboBox<Session> sessionDropDown()
    {
        if (sessionDropDown == null)
        {
            sessionDropDown = KivaKitTheme.get().applyTo(new JComboBox<>());
            sessionDropDown.addItemListener(event ->
            {
                // If the selection changes,
                if (event.getStateChange() == ItemEvent.SELECTED)
                {
                    // get the selected session,
                    final var session = (Session) sessionDropDown.getSelectedItem();
                    if (session != null)
                    {
                        // clear the log panel
                        parent.clear();

                        // load the session's log entries
                        final var entries = SessionStore.get().entries(session);
                        if (entries != null)
                        {
                            // and add any entries
                            parent.tablePanel().addAll(entries);
                        }
                    }
                }
            });
            sessionDropDown.setRenderer(new DefaultListCellRenderer()
            {

            });
        }
        return sessionDropDown;
    }

    private void updateSessionDropDown()
    {
        final var sessions = new ArrayList<>(SessionStore.get().sessions());
        sessions.sort(Comparator.naturalOrder());
        Collections.reverse(sessions);
        sessionDropDown().removeAllItems();
        for (final var session : sessions)
        {
            sessionDropDown().addItem(session);
        }
    }
}
