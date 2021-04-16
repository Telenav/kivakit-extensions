////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.ui.swing.component.dialog.message;

import javax.swing.*;
import java.awt.*;

public class MessageBox
{
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public MessageBox(final Component parent, final String text)
    {
        if (GraphicsEnvironment.isHeadless())
        {
            System.err.println("MESSAGE: " + text);
        }
        else
        {
            JOptionPane.showMessageDialog(parent, text);
        }
    }

    public MessageBox(final String text)
    {
        this(null, text);
    }
}