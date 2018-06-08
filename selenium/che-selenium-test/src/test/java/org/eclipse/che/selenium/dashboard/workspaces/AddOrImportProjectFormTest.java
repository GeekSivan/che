/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.selenium.dashboard.workspaces;

import static org.eclipse.che.selenium.pageobject.dashboard.NewWorkspace.StackId.JAVA;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import java.util.Map;
import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.client.TestProjectServiceClient;
import org.eclipse.che.selenium.core.client.TestWorkspaceServiceClient;
import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.eclipse.che.selenium.core.webdriver.SeleniumWebDriverHelper;
import org.eclipse.che.selenium.core.workspace.TestWorkspaceProvider;
import org.eclipse.che.selenium.pageobject.dashboard.Dashboard;
import org.eclipse.che.selenium.pageobject.dashboard.DocumentationPage;
import org.eclipse.che.selenium.pageobject.dashboard.NewWorkspace;
import org.eclipse.che.selenium.pageobject.dashboard.stacks.Stacks;
import org.eclipse.che.selenium.pageobject.dashboard.workspaces.WorkspaceConfig;
import org.eclipse.che.selenium.pageobject.dashboard.workspaces.WorkspaceOverview;
import org.eclipse.che.selenium.pageobject.dashboard.workspaces.WorkspaceProjects;
import org.eclipse.che.selenium.pageobject.dashboard.workspaces.Workspaces;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AddOrImportProjectFormTest {

  private static final String SPRING_SAMPLE_NAME = "web-java-spring";
  private static final String CHE_SAMPLE_NAME = "che-in-che";
  private static final String CONSOLE_SAMPLE_NAME = "console-java-simple";

  private static final Map<String, String> EXPECTED_SAMPLES_WITH_DESCRIPTIONS =
      ImmutableMap.of(
          SPRING_SAMPLE_NAME,
          "A basic example using Spring servlets. The app returns values entered into a submit form.",
          CHE_SAMPLE_NAME,
          "The Eclipse Che source code. Build Che-in-Che.",
          CONSOLE_SAMPLE_NAME,
          "A hello world Java application.");

  @Inject private Dashboard dashboard;
  @Inject private WorkspaceProjects workspaceProjects;
  @Inject private WorkspaceConfig workspaceConfig;
  @Inject private DefaultTestUser defaultTestUser;
  @Inject private Workspaces workspaces;
  @Inject private NewWorkspace newWorkspace;
  @Inject private TestWorkspaceProvider testWorkspaceProvider;
  @Inject private TestProjectServiceClient testProjectServiceClient;
  @Inject private TestWorkspaceServiceClient testWorkspaceServiceClient;
  @Inject private SeleniumWebDriverHelper seleniumWebDriverHelper;
  @Inject private SeleniumWebDriver seleniumWebDriver;
  @Inject private DocumentationPage documentationPage;
  @Inject private WorkspaceOverview workspaceOverview;
  @Inject private Stacks stacks;

  @BeforeClass
  public void setup() {
    dashboard.open();
  }

  @BeforeMethod
  public void prepareToTestMethod() {
    dashboard.waitDashboardToolbarTitle();
    dashboard.selectWorkspacesItemOnDashboard();
    workspaces.waitToolbarTitleName();
    workspaces.clickOnAddWorkspaceBtn();
    newWorkspace.waitPageLoad();
  }

  @Test
  public void checkOfCheckboxes() {
    newWorkspace.waitPageLoad();
    newWorkspace.selectStack(JAVA);
    newWorkspace.clickOnAddOrImportProjectButton();
    newWorkspace.waitAddOrImportFormOpened();
    newWorkspace.waitSamplesButtonSelected();
    newWorkspace.waitSamplesWithDescriptions(EXPECTED_SAMPLES_WITH_DESCRIPTIONS);
    waitAllCheckboxesDisabled();
    newWorkspace.waitCancelButtonInImportProjectFormDisabled();
    newWorkspace.waitAddButtonInImportProjectFormDisabled();

    newWorkspace.clickOnSampleCheckbox(CONSOLE_SAMPLE_NAME);
    newWorkspace.waitSampleCheckboxEnabled(CONSOLE_SAMPLE_NAME);
    newWorkspace.waitCancelButtonInImportProjectFormEnabled();
    newWorkspace.waitAddButtonInImportProjectFormEnabled();

    newWorkspace.clickOnCancelButtonInImportProjectForm();
    newWorkspace.waitSampleCheckboxDisabled(CONSOLE_SAMPLE_NAME);

    newWorkspace.clickOnSampleCheckbox(CHE_SAMPLE_NAME);
    newWorkspace.waitSampleCheckboxEnabled(CHE_SAMPLE_NAME);
    newWorkspace.waitCancelButtonInImportProjectFormEnabled();
    newWorkspace.waitAddButtonInImportProjectFormEnabled();

    newWorkspace.clickOnSampleCheckbox(CHE_SAMPLE_NAME);
    newWorkspace.waitSampleCheckboxDisabled(CHE_SAMPLE_NAME);
    newWorkspace.waitCancelButtonInImportProjectFormDisabled();
    newWorkspace.waitAddButtonInImportProjectFormDisabled();

    clickOnEachCheckbox();
    waitAllCheckboxesEnabled();
    newWorkspace.waitCancelButtonInImportProjectFormEnabled();
    newWorkspace.waitAddButtonInImportProjectFormEnabled();

    newWorkspace.clickOnCancelButtonInImportProjectForm();
    waitAllCheckboxesDisabled();
    newWorkspace.waitCancelButtonInImportProjectFormDisabled();
    newWorkspace.waitAddButtonInImportProjectFormDisabled();
  }

  @Test(priority = 1)
  public void checkProjectSamples() {
    newWorkspace.waitPageLoad();
    newWorkspace.selectStack(JAVA);
    newWorkspace.clickOnAddOrImportProjectButton();
    newWorkspace.waitAddOrImportFormOpened();
    newWorkspace.waitSamplesButtonSelected();
    newWorkspace.clickOnSampleCheckbox(CONSOLE_SAMPLE_NAME);
    newWorkspace.waitSampleCheckboxEnabled(CONSOLE_SAMPLE_NAME);
    newWorkspace.waitCancelButtonInImportProjectFormEnabled();
    newWorkspace.waitAddButtonInImportProjectFormEnabled();

    newWorkspace.clickOnAddButtonInImportProjectForm();
    newWorkspace.waitProjectTabAppearance(CONSOLE_SAMPLE_NAME);
  }

  private void waitAllCheckboxesDisabled() {
    newWorkspace
        .getSamplesNames()
        .forEach(sampleName -> newWorkspace.waitSampleCheckboxDisabled(sampleName));
  }

  private void waitAllCheckboxesEnabled() {
    newWorkspace
        .getSamplesNames()
        .forEach(sampleName -> newWorkspace.waitSampleCheckboxEnabled(sampleName));
  }

  private void clickOnEachCheckbox() {
    newWorkspace
        .getSamplesNames()
        .forEach(sampleName -> newWorkspace.clickOnSampleCheckbox(sampleName));
  }
}
