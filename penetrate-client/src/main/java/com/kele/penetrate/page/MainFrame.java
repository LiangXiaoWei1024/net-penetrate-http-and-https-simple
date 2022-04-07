package com.kele.penetrate.page;

import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.utils.UUIDUtils;
import lombok.Data;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

@Recognizer
@Data
public class MainFrame extends JFrame
{
    private JPanel upPanel;
    private JPanel downPanel;
    private JTextField mappingNameTextField;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JRadioButton filterMappingNameRadioButton;
    private JTextArea logTextArea;
    private JButton runButton;
    private int changeWidth;
    private int changeHeight;

    @Autowired
    private UUIDUtils uuidUtils;
    @Autowired
    private Config config;


    public MainFrame() throws HeadlessException
    {
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            changeWidth = 16;
            changeHeight = 6;
        }
        setTitle("penetrate(大家可以加一下QQ群:704592910)");
        setLayout(null);
        setSize(530 + changeWidth, 400 + changeHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setIconImage(Tray.icon);

    }

    public void init()
    {
        createUpPanel();
        createDownPanel();
        setVisible(true);
    }

    public String getIp()
    {
        return ipTextField.getText();
    }

    public String getPort()
    {
        return portTextField.getText();
    }

    public String getMappingName()
    {
        return mappingNameTextField.getText();
    }

    public Boolean isFilterMappingName()
    {
        return filterMappingNameRadioButton.isSelected();
    }

    public void setLogTextArea(List<String> logList)
    {
        StringBuilder builder = new StringBuilder();
        for (String log : logList)
        {
            builder.append("- " + log).append("\r\n");
        }
        logTextArea.setText(builder.toString());
    }

    private void createUpPanel()
    {
        upPanel = new JPanel();
        upPanel.setLayout(null);
        upPanel.setBounds(15, 15, 500, 120);
        upPanel.setBackground(new Color(255, 255, 255));

        JLabel mappingNameLabel = new JLabel("映射名称");
        mappingNameLabel.setBounds(10, 5, 100, 30);
        mappingNameTextField = new JTextField();
        mappingNameTextField.setBounds(80, 7, 200, 25);
        mappingNameTextField.setText(uuidUtils.generateShortUuid());


        JLabel filterMappingNameLabel = new JLabel("是否过滤映射名称");
        filterMappingNameLabel.setBounds(320, 5, 130, 30);
        filterMappingNameRadioButton = new JRadioButton();
        filterMappingNameRadioButton.setBounds(450, 10, 30, 20);
        filterMappingNameRadioButton.setSelected(true);


        JLabel ipLabel = new JLabel("IP");
        ipLabel.setBounds(10, 45, 50, 30);
        ipTextField = new JTextField();
        ipTextField.setBounds(80, 47, 200, 25);
        ipTextField.setText("127.0.0.1");


        JLabel portLabel = new JLabel("端口");
        portLabel.setBounds(320, 45, 50, 30);
        portTextField = new JTextField();
        portTextField.setBounds(360, 47, 130, 25);
        portTextField.setText("8080");


        runButton = new JButton("启动");
        runButton.setBounds(200, 85, 100, 25);
        runButton.addActionListener(e ->
        {
            mappingNameTextField.setEditable(false);
            portTextField.setEditable(false);
            ipTextField.setEditable(false);
            filterMappingNameRadioButton.setEnabled(false);
            runButton.setEnabled(false);
            System.out.println("点击启动");
        });

        upPanel.add(mappingNameLabel);
        upPanel.add(mappingNameTextField);
        upPanel.add(ipLabel);
        upPanel.add(ipTextField);
        upPanel.add(portLabel);
        upPanel.add(portTextField);
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
        downPanel.setBounds(15, 135, 500, 220);
        downPanel.setBackground(new Color(44, 44, 44));

        logTextArea = new JTextArea();
        logTextArea.setForeground(new Color(255, 255, 255));
        logTextArea.setBackground(new Color(44, 44, 44));
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setEditable(false);

        JScrollPane logTextAreaScrollPane = new JScrollPane(logTextArea);
        logTextAreaScrollPane.setBounds(10, 5, 480, 190);
        logTextAreaScrollPane.setBackground(new Color(44, 44, 44));
        logTextAreaScrollPane.setBorder(new LineBorder(null, 0));

        downPanel.add(logTextAreaScrollPane);
        this.add(downPanel);
    }
}
