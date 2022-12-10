/*  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.pluto.container.om.portlet.impl.jsr168;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.pluto.container.om.portlet.Description;
import org.apache.pluto.container.om.portlet.DisplayName;
import org.apache.pluto.container.om.portlet.InitParam;
import org.apache.pluto.container.om.portlet.PortletApplicationDefinition;
import org.apache.pluto.container.om.portlet.PortletDefinition;
import org.apache.pluto.container.om.portlet.PortletInfo;
import org.apache.pluto.container.om.portlet.Preference;
import org.apache.pluto.container.om.portlet.Preferences;
import org.apache.pluto.container.om.portlet.SecurityRoleRef;
import org.apache.pluto.container.om.portlet.Supports;
import org.apache.pluto.container.om.portlet.impl.ConfigurationHolder;
import org.apache.pluto.container.om.portlet.impl.DescriptionImpl;
import org.apache.pluto.container.om.portlet.impl.DisplayNameImpl;
import org.apache.pluto.container.om.portlet.impl.InitParamImpl;
import org.apache.pluto.container.om.portlet.impl.PortletDefinitionImpl;
import org.apache.pluto.container.om.portlet.impl.PortletInfoImpl;
import org.apache.pluto.container.om.portlet.impl.PreferenceImpl;
import org.apache.pluto.container.om.portlet.impl.PreferencesImpl;
import org.apache.pluto.container.om.portlet.impl.SecurityRoleRefImpl;
import org.apache.pluto.container.om.portlet.impl.SupportsImpl;
import org.apache.pluto.container.om.portlet.impl.fixtures.TestPortlet;
import org.apache.pluto.container.om.portlet.impl.fixtures.TestPreferencesValidator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PortletDefinition168ImplTest {

   private static final String XML_FILE = 
         "org/apache/pluto/container/om/portlet/portlet168Generated.xml";
   
   private static PortletApplicationDefinition pad;
   
   // Class under test
   private PortletDefinition cut;

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
      
      InputStream in = PortletApplicationDefinition168ImplTest.class
            .getClassLoader().getResourceAsStream(XML_FILE);
      
      ConfigurationHolder cfp = new ConfigurationHolder();
      try {
         cfp.processPortletDD(in);
         pad = cfp.getPad();
      } catch (Exception e) {
         e.printStackTrace();
         throw e;
      }
   }
   
   @Before
   public void setUpBefore() throws Exception {
      assertEquals(1, pad.getPortlets().size());
      cut = new PortletDefinitionImpl(pad.getPortlets().get(0));
   }

   @Test
   public void testGetPortletName() {
      assertNotNull(cut.getPortletName());
      assertEquals("portlet168", cut.getPortletName());
   }

   @Test
   public void testGetApplication() {
      assertNotNull(cut.getApplication());
      assertTrue(cut.getApplication() instanceof PortletApplicationDefinition);
   }

   @Test
   public void testGetInitParam() {
      InitParam ip = cut.getInitParam("name");
      assertNotNull(ip);
      assertEquals("name", ip.getParamName());
      assertEquals("value", ip.getParamValue());
      assertEquals(1, ip.getDescriptions().size());
      Locale loc = new Locale("de");
      Description d = ip.getDescription(loc);
      assertNotNull(d);
      assertEquals("description", d.getText());
   }

   @Test
   public void testGetInitParams() {
      List<InitParam> ips = cut.getInitParams();
      assertEquals(1, ips.size());
      InitParam ip = ips.get(0);
      assertNotNull(ip);
      assertEquals("name", ip.getParamName());
      assertEquals("value", ip.getParamValue());
   }

   @Test
   public void testAddInitParam() {
      String name = "Fred", value = "bowling";
      InitParam newip = new InitParamImpl(name, value);
      cut.addInitParam(newip);
      
      List<InitParam> ips = cut.getInitParams();
      assertEquals(2, ips.size());
      InitParam ip = cut.getInitParam(name);
      assertNotNull(ip);
      assertEquals(name, ip.getParamName());
      assertEquals(value, ip.getParamValue());
   }

   @Test
   public void testGetPortletClass() {
      assertNotNull(cut.getPortletClass());
      assertEquals(TestPortlet.class.getCanonicalName(), cut.getPortletClass());
   }

   @Test
   public void testSetPortletClass() {
      String text = "someClass";
      cut.setPortletClass(text);
      assertEquals(text, cut.getPortletClass());
   }

   @Test
   public void testGetPortletInfo() {
      PortletInfo info = cut.getPortletInfo();
      assertNotNull(info);
      assertEquals("title", info.getTitle());
      assertEquals("short-title", info.getShortTitle());
      assertEquals("keywords", info.getKeywords());
   }

   @Test
   public void testSetPortletInfo() {
      String ti = "t2", st = "st", kw = "kw";
      PortletInfo i2 = new PortletInfoImpl(ti, kw, st);
      cut.setPortletInfo(i2);

      PortletInfo info = cut.getPortletInfo();
      assertNotNull(info);
      assertEquals(ti, info.getTitle());
      assertEquals(st, info.getShortTitle());
      assertEquals(kw, info.getKeywords());
   }

   @Test
   public void testGetPortletPreferences() {
      Preferences prefs = cut.getPortletPreferences();
      assertNotNull(prefs);
      assertEquals(TestPreferencesValidator.class.getCanonicalName(), prefs.getPreferencesValidator());
      List<Preference> list = prefs.getPortletPreferences();
      assertEquals(1, list.size());
      Preference item = list.get(0);
      assertEquals("name", item.getName());
      List<String> vals = item.getValues();
      assertEquals(1, vals.size());
      assertEquals("value", vals.get(0));
   }

   @Test
   public void testSetPortletPreferences() {
      String validator = "validator";
      String name = "prefName";
      String[] vals = {"v1", "v2"};
      Preferences prefs = new PreferencesImpl(cut.getPortletPreferences());
      prefs.setPreferencesValidator(validator);
      prefs.addPreference(new PreferenceImpl(name, true, Arrays.asList(vals)));
      cut.setPortletPreferences(prefs);
      
      Preferences prefs2 = cut.getPortletPreferences();
      assertNotNull(prefs2);
      assertEquals(validator, prefs2.getPreferencesValidator());
      List<Preference> list = prefs2.getPortletPreferences();
      assertEquals(2, list.size());
      Preference item = prefs2.getPortletPreference(name);
      assertEquals(name, item.getName());
      List<String> newvals = item.getValues();
      assertEquals(2, newvals.size());
      assertArrayEquals(vals, newvals.toArray());
   }

//    @Test  // JSR 286
//    public void testGetSupportedProcessingEvents() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testAddSupportedProcessingEvent() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testGetSupportedPublishingEvents() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testAddSupportedPublishingEvent() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testGetSupportedPublicRenderParameters() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testAddSupportedPublicRenderParameter() {
//       fail("Not yet implemented");
//    }

   @Test
   public void testGetResourceBundle() {
      assertNotNull(cut.getResourceBundle());
      assertEquals("resource-bundle", cut.getResourceBundle());
   }

   @Test
   public void testSetResourceBundle() {
      String text = "newBundle";
      cut.setResourceBundle(text);
      assertNotNull(cut.getResourceBundle());
      assertEquals(text, cut.getResourceBundle());
   }

   @Test
   public void testGetSecurityRoleRef() {
      SecurityRoleRef srr = cut.getSecurityRoleRef("NMTOKEN");
      assertNotNull(srr);
      assertEquals("NMTOKEN", srr.getRoleName());
      assertEquals("role-link", srr.getRoleLink());
   }

   @Test
   public void testGetSecurityRoleRefs() {
      List<SecurityRoleRef> list = cut.getSecurityRoleRefs();
      assertEquals(1, list.size());
      SecurityRoleRef srr = list.get(0);
      assertNotNull(srr);
      assertEquals("NMTOKEN", srr.getRoleName());
      assertEquals("role-link", srr.getRoleLink());
      Description d = srr.getDescription(new Locale("de"));
      assertNotNull(d);
      assertEquals("description", d.getText());
      
   }

   @Test
   public void testAddSecurityRoleRef() {
      String name = "RoleName";
      String link = "RoleLink";
      SecurityRoleRef srr = new SecurityRoleRefImpl(name);
      srr.setRoleLink(link);
      cut.addSecurityRoleRef(srr);

      List<SecurityRoleRef> list = cut.getSecurityRoleRefs();
      assertEquals(2, list.size());
      srr = cut.getSecurityRoleRef(name);
      assertNotNull(srr);
      assertEquals(name, srr.getRoleName());
      assertEquals(link, srr.getRoleLink());
   }

   @Test
   public void testGetSupportsString() {
      Supports s = cut.getSupports("mime-type2");
      assertNotNull(s);
   }

   @Test
   public void testGetSupports() {
      List<Supports> list = cut.getSupports();
      assertEquals(3, list.size());
   }

   @Test
   public void testAddSupports() {
      Supports s = new SupportsImpl("text/html");
      cut.addSupports(s);
      List<Supports> list = cut.getSupports();
      assertEquals(4, list.size());
      boolean ok = false;
      for (Supports item : list) {
         if (item.getMimeType().equals("text/html")) {
            ok = true;
            break;
         }
      }
      assertTrue(ok);
   }

   @Test
   public void testGetDescription() {
      Locale loc = new Locale("DE");
      Description desc = cut.getDescription(loc);
      assertNotNull(desc);
      assertEquals("multi line description", desc.getText());
   }

   @Test
   public void testGetDescriptions() {
      List<Description> list = cut.getDescriptions();
      assertNotNull(list);
      assertEquals(1, list.size());
      assertEquals("multi line description", list.get(0).getText());
   }

   @Test
   public void testAddDescription() {
      Locale loc = Locale.FRENCH;
      String text = "Some description";
      Description d = new DescriptionImpl(loc, text);
      cut.addDescription(d);

      List<Description> list = cut.getDescriptions();
      assertNotNull(list);
      assertEquals(2, list.size());
      for (Description desc : list) {
         if (desc.getLocale().equals(loc)) {
            assertEquals(text, desc.getText());
         } else {
            assertEquals("multi line description", desc.getText());
         }
      }
   }

   @Test
   public void testGetDisplayName() {
      Locale loc = new Locale("DE");
      DisplayName name = cut.getDisplayName(loc);
      assertNotNull(name);
      assertEquals("display-name", name.getText());
   }

   @Test
   public void testGetDisplayNames() {
      List<DisplayName> list = cut.getDisplayNames();
      assertNotNull(list);
      assertEquals(1, list.size());
      assertEquals("display-name", list.get(0).getText());
   }

   @Test
   public void testAddDisplayName() {
      Locale loc = Locale.FRENCH;
      String text = "Some display name";
      DisplayName d = new DisplayNameImpl(loc, text);
      cut.addDisplayName(d);

      List<DisplayName> list = cut.getDisplayNames();
      assertNotNull(list);
      assertEquals(2, list.size());
      for (DisplayName desc : list) {
         if (desc.getLocale().equals(loc)) {
            assertEquals(text, desc.getText());
         } else {
            assertEquals("display-name", desc.getText());
         }
      }
   }

   @Test
   public void testGetSupportedLocales() {
      List<String> list = cut.getSupportedLocales();
      assertEquals(1, list.size());
      assertTrue(list.contains("supported-locale"));
   }

   @Test
   public void testAddSupportedLocale() {
      String locname = "zh-cmn-Hans-CN";
      cut.addSupportedLocale(locname);
      
      List<String> list = cut.getSupportedLocales();
      assertEquals(2, list.size());
      assertTrue(list.contains(locname));
   }

   @Test
   public void testGetExpirationCache() {
      assertNotNull(cut.getExpirationCache());
      assertEquals(50, cut.getExpirationCache());
   }

   @Test
   public void testSetExpirationCache() {
      cut.setExpirationCache(100);
      assertNotNull(cut.getExpirationCache());
      assertEquals(100, cut.getExpirationCache());
   }

//    @Test  // JSR 286
//    public void testGetCacheScope() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testSetCacheScope() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testGetContainerRuntimeOption() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testGetContainerRuntimeOptions() {
//       fail("Not yet implemented");
//    }
// 
//    @Test  // JSR 286
//    public void testAddContainerRuntimeOption() {
//       fail("Not yet implemented");
//    }

}
