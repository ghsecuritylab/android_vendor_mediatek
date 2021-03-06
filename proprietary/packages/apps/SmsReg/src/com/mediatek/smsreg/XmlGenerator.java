/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.mediatek.smsreg;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlGenerator {
    private static final String TAG = "SmsReg/XmlGenerator";

    private Document mDom = null;
    private List<SmsInfoUnit> mSmsInfoList = null;
    private String mOperatorName = null;
    private String mSmsNumber = null;
    private String mSmsPort = null;
    private String mSrcPort = null;
    private String[] mNetworkNumber = null;

    private static XmlGenerator sXmlGenerator = null;

    static XmlGenerator getInstance(String configName) {
        if (sXmlGenerator == null) {
            File smsRegConfig = new File(configName);
            if (!smsRegConfig.exists()) {
                throw new Error("Config file " + configName + " not exist.");
            }

            sXmlGenerator = new XmlGenerator(configName);
            if (sXmlGenerator == null) {
                throw new Error("Init XMLGenerator error!");
            }
        }
        return sXmlGenerator;
    }

    private XmlGenerator(String uri) {
        parse(uri);
        getOperatorName();
    }

    boolean parse(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            File configFile = new File(filename);
            if (!configFile.exists()) {
                Log.e(TAG, "Config file is not exist!");
            }
            mDom = builder.parse(configFile);
        } catch (ParserConfigurationException e) {
            Log.e(TAG, "Create mDom failed, " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SAXException e) {
            Log.e(TAG, "Create mDom failed, " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Create mDom failed, " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getValByTagName(Document document, String tag) {
        if (document != null) {
            Node node = document.getElementsByTagName(tag).item(0);
            if (node != null) {
                Node childnode = node.getFirstChild();
                if (childnode != null) {
                    return childnode.getNodeValue();
                }
            }
        }
        return null;
    }

    private void getValArrayFromDom(Node node, String desNodeName, ArrayList<String> strArray) {
        if (node == null) {
            NodeList nodeList = mDom.getElementsByTagName("operator");
            if (nodeList == null) {
                Log.e(TAG, "Node <operator> is not exist in the xml file.");
                return;
            }
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getAttributes().getNamedItem("xml:id").getNodeValue().equals(mOperatorName)) {
                    node = nodeList.item(i);
                    break;
                }
            }
        }

        if (node == null) {
            Log.e(TAG, "Node to read is null");
            return;
        }

        if (node.getNodeType() == Node.TEXT_NODE) { // Text node is the leaf
            Log.w(TAG, "Node to read is a leaf.");
            return;
        }

        String nodeName = node.getNodeName();
        Log.i(TAG, "Node to read: " + nodeName);
        if (nodeName.equalsIgnoreCase(desNodeName)) {
            for (; node != null; ) {
                Node siblingNode = node.getFirstChild();
                while (siblingNode != null) {
                    Log.i(TAG, "sibling node: " + siblingNode.getNodeName());
                    if (siblingNode.getNodeType() != Node.TEXT_NODE) {
                        if (siblingNode.getFirstChild() == null) {
                            strArray.add(siblingNode.getNodeValue());
                            Log.i(TAG, "found NodeValue: " + siblingNode.getNodeValue());
                        } else {
                            String temp = siblingNode.getFirstChild().getNodeValue();
                            if (temp != null) {
                                strArray.add(temp);
                            }
                            Log.i(TAG, "found NodeValue: " + temp);
                        }
                    }
                    try {
                        siblingNode = siblingNode.getNextSibling();
                        if (siblingNode != null) {
                            Log.i(TAG, "siblingNode NodeName: " + siblingNode.getNodeName());
                        }
                    } catch (IndexOutOfBoundsException e) {
                        siblingNode = null;
                    }
                }
                try {
                    node = node.getNextSibling();
                    if (node != null) {
                        Log.i(TAG, "node NodeName:-- " + node.getNodeName());
                    }
                } catch (IndexOutOfBoundsException e) {
                    node = null;
                }
            }
            return;
        } else {
            if (node.hasChildNodes()) {
                getValArrayFromDom(node.getFirstChild(), desNodeName, strArray);
            }
            Node sibling = null;
            try {
                sibling = node.getNextSibling();
            } catch (IndexOutOfBoundsException e) {
                sibling = null;
            }
            if (sibling != null) {
                getValArrayFromDom(sibling, desNodeName, strArray);
            }
            return;
        }
    }

    public boolean getCustomizedStatus() {
        return (getValByTagName(mDom, "customized").equalsIgnoreCase("on"));
    }

    public String getOperatorName() {
        if (mOperatorName == null) {
            mOperatorName = getValByTagName(mDom, "operatorcustomized");
        }
        return mOperatorName;
    }

    public String getOemName() {
        if (mOperatorName == null) {
            mOperatorName = getValByTagName(mDom, "oemname");
        }
        return mOperatorName;
    }

    public String getSmsNumber() {
        if (mSmsNumber == null) {
            mSmsNumber = getValByTagName(mDom, "smsnumber");
        }
        return mSmsNumber;
    }

    public Short getSmsPort() {
        if (mSmsPort == null) {
            mSmsPort = getValByTagName(mDom, "smsport");
        }
        return Short.parseShort(mSmsPort);
    }

    public Short getSrcPort() {
        if (mSrcPort == null) {
            mSrcPort = getValByTagName(mDom, "srcport");
        }
        return Short.parseShort(mSrcPort);
    }

    public String[] getNetworkNumber() {
        if (mNetworkNumber == null) {
            ArrayList<String> valueArray = new ArrayList<String>();
            getValArrayFromDom(null, "networkNumber", valueArray);
            mNetworkNumber = new String[valueArray.size()];
            valueArray.toArray(mNetworkNumber);
        }
        return mNetworkNumber;
    }

    public List<SmsInfoUnit> getSmsInfoList() {
        if (mSmsInfoList == null) {
            ArrayList<String> valueArray = new ArrayList<String>();
            getValArrayFromDom(null, "segment", valueArray);
            String[] smsArray = new String[valueArray.size()];
            valueArray.toArray(smsArray);
            mSmsInfoList = new ArrayList<SmsInfoUnit>();
            for (int i = 0; (3 * i + 1) < smsArray.length && smsArray[3 * i + 1] != null; i++) {
                SmsInfoUnit smsUnit = new SmsInfoUnit();
                if (3 * i < smsArray.length) {
                    smsUnit.setPrefix(smsArray[3 * i]);
                }
                if ((3 * i + 1) < smsArray.length) {
                    smsUnit.setContent(smsArray[3 * i + 1]);
                }
                if ((3 * i + 2) < smsArray.length) {
                    smsUnit.setPostfix(smsArray[3 * i + 2]);
                }
                mSmsInfoList.add(smsUnit);
            }
        }
        return mSmsInfoList;
    }
}

class SmsInfoUnit {
    private String mPrefix = null;
    private String mContent = null;
    private String mPostfix = null;

    void setPrefix(String preStr) {
        mPrefix = preStr;
    }

    void setContent(String cntStr) {
        mContent = cntStr;
    }

    void setPostfix(String postStr) {
        mPostfix = postStr;
    }

    String getPrefix() {
        return mPrefix;
    }

    String getContent() {
        return mContent;
    }

    String getPostfix() {
        return mPostfix;
    }
}
