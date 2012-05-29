package com.intellij.lang.javascript.flex.actions.airpackage;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;

@State(
  name = "AirPackageProjectParameters",
  storages = {
    @Storage(file = StoragePathMacros.WORKSPACE_FILE)
  }
)
public class AirPackageProjectParameters implements PersistentStateComponent<AirPackageProjectParameters> {
  private static final String NATIVE_INSTALLER_EXTENSION =
    SystemInfo.isWindows ? ".exe"
                         : SystemInfo.isMac ? ".dmg"
                                            : SystemInfo.OS_NAME.toLowerCase().contains("ubuntu") ? ".deb"
                                                                                                  : ".rpm";

  public enum DesktopPackageType {
    AirInstaller("installer (*.air)", ".air"),
    NativeInstaller("native installer (*" + NATIVE_INSTALLER_EXTENSION + ")", NATIVE_INSTALLER_EXTENSION),
    CaptiveRuntimeBundle("captive runtime bundle" + (SystemInfo.isMac ? " (*.app)" : ""), SystemInfo.isMac ? ".app" : ""),
    Airi("unsigned package (*.airi)", ".airi");

    DesktopPackageType(final String presentableName, final String extension) {
      myPresentableName = presentableName;
      myFileExtension = extension;
    }

    private final String myPresentableName;
    private final String myFileExtension;

    public String toString() {
      return myPresentableName;
    }

    public String getFileExtension() {
      return myFileExtension;
    }
  }

  public enum AndroidPackageType {
    Release("release"),
    DebugOverUSB("debug over USB"),
    DebugOverNetwork("debug over network");

    AndroidPackageType(final String presentableName) {
      this.myPresentableName = presentableName;
    }

    private final String myPresentableName;

    public String toString() {
      return myPresentableName;
    }
  }

  public enum IOSPackageType {
    Test("test without debugging"),
    DebugOverNetwork("debug over network"),
    AdHoc("ad hoc distribution"),
    AppStore("Apple App Store distribution");

    IOSPackageType(final String presentableName) {
      this.myPresentableName = presentableName;
    }

    private final String myPresentableName;

    public String toString() {
      return myPresentableName;
    }
  }

  public DesktopPackageType desktopPackageType = DesktopPackageType.values()[0];

  public AndroidPackageType androidPackageType = AndroidPackageType.values()[0];
  public boolean apkCaptiveRuntime = true;
  public int apkDebugListenPort = AirPackageUtil.DEBUG_PORT_DEFAULT;

  public IOSPackageType iosPackageType = IOSPackageType.values()[0];
  public boolean iosFastPackaging = true;

  //public Collection<String> selectedPaths

  @Transient
  private boolean myPackagingInProgress = false;

  @Transient
  private PasswordStore myPasswordStore = new PasswordStore();

  public static AirPackageProjectParameters getInstance(final Project project) {
    return ServiceManager.getService(project, AirPackageProjectParameters.class);
  }

  public static PasswordStore getPasswordStore(final Project project) {
    return getInstance(project).myPasswordStore;
  }

  public AirPackageProjectParameters getState() {
    return this;
  }

  public void loadState(final AirPackageProjectParameters state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public void setPackagingInProgress(final boolean packagingInProgress) {
    myPackagingInProgress = packagingInProgress;
  }

  public boolean isPackagingInProgress() {
    return myPackagingInProgress;
  }
}