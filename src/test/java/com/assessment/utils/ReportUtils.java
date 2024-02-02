package com.assessment.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.shared.utils.xml.pull.XmlPullParserException;

public class ReportUtils {
	public static final String REPORT_DIR = "report";
	public static final String SELENIUM_ARTIFACT = "selenium-java";
	public static final String CUCUMBER_ARTIFACT = "cucumber-junit";
	public static final String EXTENTREPORT_ARTIFACT = "extentreports-cucumber7-adapter";
	public static final File EXTENT_CONFIG_FILEPATH = Paths.get("src", "test", "resources", "extent-config.xml").toFile();
	
	public static String reportPath;
	public static ReportUtils constants;

	public static ReportUtils getReportData() {
		if (constants == null) {
			constants = new ReportUtils();
		}
		return constants;
	}

	private ReportUtils() {
	}

	public static File getReportFile() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss_dd_MMM_YYYY");
		LocalDateTime now = LocalDateTime.now();

		if (!new File(Paths.get(REPORT_DIR).toString()).exists())
			new File(Paths.get("").toAbsolutePath().toString() + File.separator + REPORT_DIR).mkdir();

		reportPath = Paths.get(REPORT_DIR).toString() + File.separator + "Report_" + dtf.format(now) + ".html";
		File reportFilePath = new File(reportPath);
		return reportFilePath;
	}

	
	public static Map<String, String> getSystemInfo()
			throws FileNotFoundException, IOException, XmlPullParserException, org.codehaus.plexus.util.xml.pull.XmlPullParserException {
		final Map<String, String> systemInfo = new LinkedHashMap<String, String>();
		systemInfo.put("User Name",System.getProperty("user.name"));
		systemInfo.put("OS", System.getProperty("os.name").toLowerCase());
		systemInfo.put("Java Version",System.getProperty("java.version"));
		systemInfo.put("Host Name",java.net.InetAddress.getLocalHost().getHostName());
		systemInfo.put("Selenium version", getPOMDependencyVersion(SELENIUM_ARTIFACT));
		systemInfo.put("Cucumber-Junit version", getPOMDependencyVersion(CUCUMBER_ARTIFACT));
		systemInfo.put("Extent Cucumber Adapter version", getPOMDependencyVersion(EXTENTREPORT_ARTIFACT));
		return systemInfo;
	}

	public static String getPOMDependencyVersion(String artifactID)
			throws FileNotFoundException, IOException, XmlPullParserException, org.codehaus.plexus.util.xml.pull.XmlPullParserException {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = reader.read(new FileReader("pom.xml"));
		String dependencyVersion = "";

		for (Dependency dependency : model.getDependencies()) {
			if (dependency.getArtifactId().equalsIgnoreCase(artifactID)) {
				String version = dependency.getVersion();

				if (version.startsWith("$")) {
					String property = version.substring(2, version.length() - 1);
					dependencyVersion = model.getProperties().get(property).toString();
				} else {
					dependencyVersion = version;
				}

			}
		}
		return dependencyVersion;
	}
}
