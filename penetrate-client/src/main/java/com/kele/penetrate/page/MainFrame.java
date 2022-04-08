package com.kele.penetrate.page;

import com.kele.penetrate.client.ConnectHandler;
import com.kele.penetrate.config.Config;
import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.protocol.CancelMapping;
import com.kele.penetrate.protocol.Handshake;
import com.kele.penetrate.utils.CheckUtils;
import com.kele.penetrate.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
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
    private JScrollPane logTextAreaScrollPane;
    private int changeWidth;
    private int changeHeight;

    @Autowired
    private UUIDUtils uuidUtils;
    @Autowired
    private Config config;
    @Autowired
    private ClientLogPageManager clientLogPageManager;
    @Autowired
    private ConnectHandler connectHandler;
    @Autowired
    private CheckUtils checkUtils;


    public MainFrame() throws HeadlessException
    {
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
        {
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

    public void setLogTextArea(List<ClientLogPageManager.LogInfo> logList)
    {
        StringBuilder builder = new StringBuilder();
        for (ClientLogPageManager.LogInfo logInfo : logList)
        {
            builder.append(logInfo.getTime()).append(" - ").append(logInfo.getMsg()).append("\r\n");
        }
        logTextArea.setText(builder.toString());
    }


    public void setAllEditable(boolean isEditable)
    {
        mappingNameTextField.setEditable(isEditable);
        portTextField.setEditable(isEditable);
        ipTextField.setEditable(isEditable);
        filterMappingNameRadioButton.setEnabled(isEditable);
        runButton.setEnabled(isEditable);
    }

    private void createUpPanel()
    {
        upPanel = new JPanel();
        upPanel.setLayout(null);
        upPanel.setBounds(15, 15, 500, 120);
        upPanel.setBackground(new Color(255, 255, 255));

        Font font = new Font("宋体", Font.PLAIN, 12);

        JLabel mappingNameLabel = new JLabel("映射名称");
        mappingNameLabel.setFont(font);
        mappingNameLabel.setBounds(10, 5, 100, 30);
        mappingNameTextField = new JTextField();
        mappingNameTextField.setBounds(80, 7, 200, 25);
        mappingNameTextField.setFont(font);
        mappingNameTextField.setText(uuidUtils.generateShortUuid());


        JLabel filterMappingNameLabel = new JLabel("是否过滤映射名称");
        filterMappingNameLabel.setFont(font);
        filterMappingNameLabel.setBounds(320, 5, 130, 30);
        filterMappingNameRadioButton = new JRadioButton();
        filterMappingNameRadioButton.setBounds(450, 10, 30, 20);
        filterMappingNameRadioButton.setSelected(true);


        JLabel ipLabel = new JLabel("IP");
        ipLabel.setFont(font);

        ipLabel.setBounds(10, 45, 50, 30);
        ipTextField = new JTextField();
        ipTextField.setBounds(80, 47, 200, 25);
        ipTextField.setFont(font);
        ipTextField.setText("127.0.0.1");


        JLabel portLabel = new JLabel("端口");
        portLabel.setFont(font);
        portLabel.setBounds(320, 45, 50, 30);
        portTextField = new JTextField();
        portTextField.setBounds(360, 47, 130, 25);
        portTextField.setFont(font);
        portTextField.setText("8080");


        runButton = new JButton("启动");
        runButton.setFont(font);
        runButton.setBounds(200, 85, 100, 25);
        runButton.addActionListener(e ->
        {
            setAllEditable(false);

            String butText = runButton.getText();
            if ("启动".equals(butText))
            {
                String mappingName = getMappingName();
                String ip = getIp();
                String port = getPort();
                boolean isFilterMappingName = isFilterMappingName();
                if (!connectHandler.isConnect())
                {
                    clientLogPageManager.addLog("与服务器失败,不能成功启动...");
                    setAllEditable(true);
                    return;
                }

                if (!checkUtils.checkMappingName(mappingName))
                {
                    clientLogPageManager.addLog("请检查映射名称是否正确(不能含有特殊字符，长度>0)");
                    setAllEditable(true);
                    return;
                }
                if (!checkUtils.checkPort(port))
                {
                    clientLogPageManager.addLog("请检查端口是否正确");
                    setAllEditable(true);
                    return;
                }
                if (!checkUtils.checkIp(ip))
                {
                    clientLogPageManager.addLog("ip地址不能为空");
                    setAllEditable(true);
                    return;
                }

                Handshake handshake = new Handshake();
                handshake.setVersion(config.getVersion());
                handshake.setMappingIp(ip);
                handshake.setPort(Integer.parseInt(port));
                handshake.setFilterMappingName(isFilterMappingName);
                handshake.setMappingName(mappingName);
                connectHandler.setHandshake(handshake);
                connectHandler.send(handshake);
                clientLogPageManager.addLog("发送启动请求");
            }
            else
            {
                setAllEditable(false);
                if (connectHandler.getChannel() == null || !connectHandler.getChannel().isActive())
                {
                    clientLogPageManager.addLog("与服务器连接断开，请稍后重试...");
                    runButton.setEnabled(true);
                    return;
                }
                clientLogPageManager.addLog("发送暂停请求");
                connectHandler.setHandshake(null);
                connectHandler.send(new CancelMapping());
            }
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
        downPanel.setLocation(15, 135);
        downPanel.setBackground(new Color(44, 44, 44));

        logTextArea = new JTextArea();
        logTextArea.setForeground(new Color(255, 255, 255));
        logTextArea.setBackground(new Color(44, 44, 44));
        logTextArea.setFont(new Font("宋体", Font.PLAIN, 10));
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setEditable(false);

        logTextAreaScrollPane = new JScrollPane(logTextArea);
        logTextAreaScrollPane.setBounds(10, 5, 480, 210);
        logTextAreaScrollPane.setBackground(new Color(44, 44, 44));
        logTextAreaScrollPane.setBorder(new LineBorder(null, 0));

        downPanel.add(logTextAreaScrollPane);
        this.add(downPanel);
    }
}
