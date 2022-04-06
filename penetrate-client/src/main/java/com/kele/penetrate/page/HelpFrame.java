package com.kele.penetrate.page;

import com.kele.penetrate.factory.annotation.Recognizer;

import javax.swing.*;
import java.awt.*;

@Recognizer
public class HelpFrame extends JFrame
{
    public HelpFrame() throws HeadlessException
    {
        setTitle("帮助");
        setLayout(null);
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}
