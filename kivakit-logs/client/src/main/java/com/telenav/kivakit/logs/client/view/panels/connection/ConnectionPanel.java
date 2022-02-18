package com.telenav.kivakit.logs.client.view.panels.connection;

import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.logs.client.network.Connector;
import com.telenav.kivakit.logs.server.ServerLog;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.client.ServiceRegistryClient;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.JComboBox;
import java.awt.FlowLayout;

/**
 * @author jonathanl (shibo)
 */
public class ConnectionPanel extends KivaKitPanel
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** Dropdown of services to connect to */
    private JComboBox<Service> connectComboBox;

    /** Connects to the selected log service */
    private final Connector connector;

    private boolean updatingComboBox;

    public ConnectionPanel(Connector connector)
    {
        this.connector = connector;

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(KivaKitTheme.get().newComponentLabel("Connection:"));
        add(connectComboBox());
    }

    private JComboBox<Service> connectComboBox()
    {
        if (connectComboBox == null)
        {
            connectComboBox = KivaKitTheme.get().applyTo(new JComboBox<>());
            connectComboBox.setEditable(false);
            connectComboBox.addActionListener(event ->
            {
                if (!updatingComboBox)
                {
                    connectTo((Service) connectComboBox.getSelectedItem());
                }
                updatingComboBox = false;
            });

            KivaKitThread.run("LogConnectionRefresher", () -> Duration.seconds(15).loop(this, this::refreshConnections));
        }

        return connectComboBox;
    }

    private void connectTo(Service service)
    {
        if (service != null)
        {
            information("Connecting to $", service);
            KivaKitThread.run("LogConnector", () -> connector.connect(service.port()));
        }
        else
        {
            KivaKitThread.run("LogDisconnector", connector::disconnect);
        }
    }

    private void refreshConnections()
    {
        // Locate all KivaKit server logs services on the network
        var client = LOGGER.listenTo(new ServiceRegistryClient());
        var services = client.discoverServices(Scope.localhost(), ServerLog.SERVER_LOG)
                .apply(client.discoverServices(Scope.network(), ServerLog.SERVER_LOG), Sets::union);
        if (services.isPresent())
        {
            // and add them to the dropdown, preserving the selected service if possible
            updatingComboBox = true;
            var selected = (Service) connectComboBox.getSelectedItem();
            connectComboBox().removeAllItems();
            connectComboBox().addItem(null);
            for (var service : ObjectList.objectList(services.get()).sorted())
            {
                connectComboBox().addItem(service);
            }
            connectComboBox.setSelectedItem(selected);
        }
    }
}
