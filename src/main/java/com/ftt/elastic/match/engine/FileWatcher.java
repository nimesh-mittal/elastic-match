/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.engine;

import com.ftt.elastic.match.ingest.CSVDataImporter;
import com.ftt.elastic.match.startup.StartupSettings;
import com.ftt.elastic.match.beans.MatchConfig;
import com.ftt.elastic.match.db.MatchConfigDAO;
import com.ftt.elastic.match.exception.BadConfigException;
import com.ftt.elastic.match.utils.SystemHealthUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.pmw.tinylog.Logger;

/**
 *
 * @author nimeshagarwal
 */
public class FileWatcher {

    public FileWatcher() {
        StartupSettings.initBatch();
    }

    public void start() throws BadConfigException, InterruptedException {
        while (true) {
            MatchConfigDAO matchConfigDAO = new MatchConfigDAO();
            List<MatchConfig> matchConfigs = matchConfigDAO.filter(new HashMap<>(), null, null);
            for (MatchConfig matchConfig : matchConfigs) {
                watch(matchConfig.getSideADirectoryPath());
                watch(matchConfig.getSideBDirectoryPath());
                SystemHealthUtils.create("Running", "import");
            }
            Thread.sleep(1000 * 10);
        }
    }

    public void watch(String directoryPath) throws BadConfigException {
        if(!new File(directoryPath).exists()){
            Logger.info("directory [{}] does not exist", directoryPath);
            SystemHealthUtils.create("Warning", "import");
            return;
        }
        
        File file = pickFile(directoryPath, new ArrayList<>());
        if (file == null) {
            Logger.info("no file to read from directory [{}]", directoryPath);
            return;
        }

        String workingFilePath = directoryPath + "../working/" + file.getName();
        String archiveFilePath = directoryPath + "../archive/" + file.getName();
        String fileExtention = getFileExtension(file);

        if (fileExtention.equalsIgnoreCase(".csv")) {
            Logger.info("moving file [{}] in to working folder", file.getAbsolutePath());

            File workingFile = new File(workingFilePath);
            if (file.renameTo(workingFile)) {
                if (importData(directoryPath, workingFilePath)) {
                    if (workingFile.renameTo(new File(archiveFilePath))) {
                        Logger.info("moving file [{}] in to archive folder", workingFile.getAbsolutePath());
                    } else {
                        Logger.warn("unable to move file into archive directory {}", archiveFilePath);
                    }
                } else {
                    Logger.warn("error in processing file {}", file.getAbsoluteFile());
                }
            } else {
                Logger.warn("unable to move file into working directory {}", workingFilePath);
            }
        } else {
            Logger.warn("unsupported file extension for file {}", file.getAbsoluteFile());
        }
    }

    private String getFileExtension(File file) {
        return file.getName().substring(file.getName().lastIndexOf('.'));
    }

    public File pickFile(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();

        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
                return file;
            } else if (file.isDirectory()) {
                pickFile(file.getAbsolutePath(), files);
            }
        }
        return files.isEmpty() ? null : files.get(0);
    }

    private boolean importData(String directoryPath, String workingFilePath) throws BadConfigException {
        boolean importStatus = false;
        CSVDataImporter dataImportEngine = new CSVDataImporter(directoryPath);
        try {
            importStatus = dataImportEngine.importFile(workingFilePath);
        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            Logger.error(ex, "error in data import");
        }
        return importStatus;
    }
}
