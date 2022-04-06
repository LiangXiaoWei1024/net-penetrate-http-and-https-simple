package com.kele.penetrate.page;

import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.utils.UUIDUtils;
import lombok.Data;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

@Recognizer
@Data
public class MainFrame extends JFrame
{
    private JPanel upPanel;
    private JPanel downPanel;

    public MainFrame() throws HeadlessException
    {
        setTitle("penetrate");
        setLayout(null);
        setSize(516, 389);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        createUpPanel();
        createDownPanel();
        setVisible(true);
    }

    private void createUpPanel()
    {
        upPanel = new JPanel();
        upPanel.setLayout(null);
        upPanel.setSize(500, 120);
        upPanel.setLocation(0, 0);
        upPanel.setBackground(new Color(255, 255, 255));

        JLabel mappingNameLabel = new JLabel("映射名称");
        mappingNameLabel.setBounds(10, 5, 100, 30);
        JTextField mappingNameField = new JTextField();
        mappingNameField.setBounds(80, 5, 200, 30);
        mappingNameField.setText(UUIDUtils.generateShortUuid());
//        mappingNameField.setFocusable(false);


        JLabel filterMappingNameLabel = new JLabel("是否过滤映射名称");
        filterMappingNameLabel.setBounds(320, 5, 130, 30);
        JRadioButton filterMappingNameRadioButton = new JRadioButton();
        filterMappingNameRadioButton.setBounds(450, 10, 20, 20);
        filterMappingNameRadioButton.setSelected(true);
//        filterMappingNameRadioButton.setFocusable(false);


        JLabel ipLabel = new JLabel("IP");
        ipLabel.setBounds(10, 45, 50, 30);
        JTextField ipField = new JTextField();
        ipField.setBounds(80, 45, 200, 30);
        ipField.setText("127.0.0.1");
//        ipField.setFocusable(false);


        JLabel portLabel = new JLabel("端口");
        portLabel.setBounds(320, 45, 50, 30);
        JTextField portField = new JTextField();
        portField.setBounds(360, 45, 130, 30);
        portField.setText("8080");
//        portField.setFocusable(false);


        JButton runButton = new JButton("启动");
        runButton.setBounds(200, 80, 120, 35);
//        portField.setFocusable(false);


        upPanel.add(mappingNameLabel);
        upPanel.add(mappingNameField);
        upPanel.add(ipLabel);
        upPanel.add(ipField);
        upPanel.add(portLabel);
        upPanel.add(portField);
        upPanel.add(filterMappingNameLabel);
        upPanel.add(filterMappingNameRadioButton);
        upPanel.add(runButton);
        upPanel.setFocusable(true);
        this.add(upPanel);
    }

    private void createDownPanel()
    {
        downPanel = new JPanel();
        downPanel.setLayout(null);
        downPanel.setSize(500, 250);
        downPanel.setLocation(0, 100);
        downPanel.setBackground(new Color(44, 44, 44));


        JTextArea jta=new JTextArea();
        jta.setForeground(new Color(255, 255, 255));
        jta.setBackground(new Color(44, 44, 44));
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        jta.setEditable(false);

        JScrollPane jScrollPane = new JScrollPane(jta);
        jScrollPane.setBounds(10,25,480,190);
        jScrollPane.setBackground(new Color(44, 44, 44));
        jScrollPane.setBorder(new LineBorder(null,0));

        jta.setText("aaaaaaaaaaa");

        downPanel.add(jScrollPane);
        this.add(downPanel);
    }
}
