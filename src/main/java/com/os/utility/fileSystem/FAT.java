package com.os.utility.fileSystem;

import com.os.applications.fileApp.application.FileApplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.os.applications.processApp.processSystem.ExecutableFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.os.applications.processApp.processSystem.ProcessManager;

public class FAT implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int DISK_NUM = 256;
    private static ObservableList<File> openedFiles;  // 记录已打开的文件
    private Disk[] disks;
    private final Folder c;
    private List<Path> paths;

    public FAT() {
        File f1 = new File("FAT", null, 0, null);
        this.disks = new Disk[DISK_NUM];
        this.disks[0] = new Disk(0, 1, "文件", f1);
        this.disks[0].setBegin(true);
        this.disks[1] = new Disk(1, -1, "文件", f1);
        this.c = new Folder("C:", "root", 2, null);
        this.disks[2] = new Disk(2, -1, "root", this.c);
        this.disks[2].setBegin(true);

        int i;
        for (i = 3; i < 3 + ProcessManager.executableFileList.size(); ++i) {
            ExecutableFile f = ProcessManager.executableFileList.get(i - 3);
            this.disks[i] = new Disk(i, -1, "可执行文件", f);
            this.disks[i].allocBlock(-1, "可执行文件", f, true);
            this.disks[i].setBegin(true);
        }

        for (i = 3 + ProcessManager.executableFileList.size(); i < 256; ++i) {
            this.disks[i] = new Disk(i, 0, "空", null);
        }

        openedFiles = FXCollections.observableArrayList(new ArrayList<>());
        this.paths = new ArrayList<>();
        Path rootPath = new Path("C:", null);
        this.paths.add(rootPath);
        this.c.setPath(rootPath);
    }

    public void addOpenedFile(Disk block) {
        File thisFile = (File) block.getObject();
        openedFiles.add(thisFile);
        thisFile.setOpened(true);
    }

    public static void removeOpenedFile(Disk block) {
        File thisFile = (File) block.getObject();

        for (int i = 0; i < openedFiles.size(); ++i) {
            if (openedFiles.get(i) == thisFile) {
                openedFiles.remove(i);
                thisFile.setOpened(false);
                break;
            }
        }

    }

    // 关闭所有已打开的文件
    public static void closeAll() {
        if (openedFiles != null) {
            for (int i = openedFiles.size() - 1; i >= 0; --i) {
                openedFiles.get(i).setOpened(false);
                openedFiles.remove(i);
            }
        }

    }

    public boolean isOpenedFile(Disk block) {
        return !(block.getObject() instanceof Folder) &&
                ((File) block.getObject()).isOpened();
    }

    public int createFolder(String path) {
        String folderName;
        boolean canName;
        int index = 1;

        int num;
        Folder parent;
        do {
            folderName = "文件夹";
            canName = true;
            folderName = folderName + index;

            for (num = 3 + ProcessManager.executableFileList.size(); num < this.disks.length; ++num) {
                if (!this.disks[num].isFree() && this.disks[num].getType().equals("文件夹")) {
                    parent = (Folder) this.disks[num].getObject();
                    if (path.equals(parent.getLocation()) && folderName.equals(parent.getName())) {
                        canName = false;
                    }
                }
            }

            ++index;
        } while (!canName);

        num = this.searchEmptyDiskBlock();
        if (num == -1) {
            return -1;
        } else {
            parent = this.getFolder(path);
            if (path.equals("C:")) {
                parent.setCatalogNum(parent.getCatalogNum() + 1);
            } else {
                parent.setCatalogNum(parent.getCatalogNum() + 1);

                int countBlock;
                if (parent.getCatalogNum() % 8 == 0) {
                    countBlock = parent.getCatalogNum() / 8;
                } else {
                    countBlock = parent.getCatalogNum() / 8 + 1;
                }

                this.reallocBlocks(countBlock, this.disks[parent.getDiskNum()]);
            }

            num = this.searchEmptyDiskBlock();
            Folder newFolder = new Folder(folderName, path, num, parent);
            parent.addChildren(newFolder);

            this.disks[num].allocBlock(-1, "文件夹", newFolder, true);
            Path parentPath = this.getPath(path);
            Path thisPath = new Path(path + "\\" + folderName, parentPath);
            if (parentPath != null) {
                parentPath.addChildren(thisPath);
            }

            this.paths.add(thisPath);
            newFolder.setPath(thisPath);
            return num;
        }
    }

    public int createFile(String path) {
        String fileName;
        boolean canName;
        int index = 1;

        int num;
        do {
            fileName = "文件";
            canName = true;
            fileName = fileName + index;

            for (num = 3; num < this.disks.length; ++num) {
                if (!this.disks[num].isFree() && this.disks[num].getType().equals("文件")) {
                    File file = (File) this.disks[num].getObject();
                    if (path.equals(file.getLocation()) && fileName.equals(file.getName())) {
                        canName = false;
                    }
                }
            }

            ++index;
        } while (!canName);

        num = this.searchEmptyDiskBlock();
        if (num == -1) {
            return -1;
        } else {
            Folder parent = this.getFolder(path);
            parent.setCatalogNum(parent.getCatalogNum() + 1);
            if (!path.equals("C:")) {
                int countBlock;
                if (parent.getCatalogNum() % 8 == 0) {
                    countBlock = parent.getCatalogNum() / 8;
                } else {
                    countBlock = parent.getCatalogNum() / 8 + 1;
                }

                this.reallocBlocks(countBlock, this.disks[parent.getDiskNum()]);
            }

            num = this.searchEmptyDiskBlock();
            File file = new File(fileName, path, num, parent);
            file.setFlag(1);
            if (FileApplication.copyFlag) {
                file.setContent(FileApplication.copyFile.getContent());
                boolean canName1;
                index = 1;

                int newLength;
                do {
                    fileName = FileApplication.copyFile.getName();
                    canName1 = true;
                    fileName = fileName + index;

                    for (newLength = 3 + ProcessManager.executableFileList.size(); newLength < this.disks.length; ++newLength) {
                        if (!this.disks[newLength].isFree() && this.disks[newLength].getType().equals("文件")) {
                            File file1 = (File) this.disks[newLength].getObject();
                            if (path.equals(file1.getLocation()) && fileName.equals(file1.getName())) {
                                canName1 = false;
                                break;
                            }
                        }
                    }

                    ++index;
                } while (!canName1);

                file.setName(fileName);
                newLength = FileApplication.copyFile.getContent().length();
                int blockCount = blocksCount(newLength);
                file.setLength(blockCount);
                file.setContent(FileApplication.copyFile.getContent());
                file.setSize(getSize(newLength));
                if (file.hasParent()) {
                    Folder parent1 = file.getParent();
                    parent1.setSize(getFolderSize(parent1));

                    while (parent1.hasParent()) {
                        parent1 = parent1.getParent();
                        parent1.setSize(getFolderSize(parent1));
                    }
                }

                if (FileApplication.moveFlag) {
                    boolean canName2;
                    index = 1;

                    while (true) {
                        fileName = FileApplication.copyFile.getName();
                        canName2 = true;
                        fileName = fileName + index;

                        for (int i = 3 + ProcessManager.executableFileList.size(); i < this.disks.length; ++i) {
                            if (!this.disks[i].isFree() && this.disks[i].getType().equals("文件")) {
                                File file2 = (File) this.disks[i].getObject();
                                if (path.equals(file2.getLocation()) && fileName.equals(file2.getName())) {
                                    canName2 = false;
                                    break;
                                }
                            }
                        }

                        ++index;
                        if (canName2) {
                            file.setName(fileName);
                            break;
                        }
                    }
                }

                this.reallocBlocks(blockCount, FileApplication.copyBlock);
            }

            parent.addChildren(file);

            this.disks[num].allocBlock(-1, "文件", file, true);
            return num;
        }
    }

    public int searchEmptyDiskBlock() {
        for (int i = 3 + ProcessManager.executableFileList.size(); i < this.disks.length; ++i) {
            if (this.disks[i].isFree()) {
                return i;
            }
        }

        return -1;
    }

    public int freeBlocksCount() {
        int n = 0;

        for (Disk disk : this.disks) {
            if (disk.isFree()) {
                ++n;
            }
        }

        return n;
    }

    public boolean reallocBlocks(int num, Disk block) {
        BaseFile thisFile = (BaseFile) block.getObject();
        int begin = thisFile.getDiskNum();
        int index = this.disks[begin].getIndex();

        int oldNum;
        for (oldNum = 1; index != -1; index = this.disks[index].getIndex()) {
            ++oldNum;
            if (this.disks[index].getIndex() == -1) {
                begin = index;
            }
        }

        int end;
        int next;
        int i;
        if (num > oldNum) {
            end = num - oldNum;
            if (this.freeBlocksCount() < end) {
                return false;
            }

            next = this.searchEmptyDiskBlock();
            this.disks[begin].setIndex(next);

            for (i = 1; i <= end; ++i) {
                next = this.searchEmptyDiskBlock();
                String type = thisFile instanceof File ? "文件" : "文件夹";
                this.disks[next].allocBlock(-1, type, thisFile, false);
                if (i != end) {
                    int space2 = this.searchEmptyDiskBlock();
                    this.disks[next].setIndex(space2);
                }
            }
        } else if (num < oldNum) {
            for (end = thisFile.getDiskNum(); num > 1; --num) {
                end = this.disks[end].getIndex();
            }

            for (i = this.disks[end].getIndex(); i != -1; i = next) {
                next = this.disks[i].getIndex();
                this.disks[i].clearBlock();
            }

            this.disks[end].setIndex(-1);
        }

        if (thisFile instanceof File)
            thisFile.setLength(num);

        return true;
    }

    public List<Folder> getFolders(String path) {
        List<Folder> list = new ArrayList<>();

        for (int i = 3 + ProcessManager.executableFileList.size(); i < this.disks.length; ++i) {
            if (!this.disks[i].isFree() && this.disks[i].getObject() instanceof Folder && ((Folder) this.disks[i].getObject()).getLocation().equals(path)) {
                list.add((Folder) this.disks[i].getObject());
            }
        }

        return list;
    }

    public List<Disk> getBlockList(String path) {
        List<Disk> List = new ArrayList<>();

        int n;
        for (n = 3 + ProcessManager.executableFileList.size(); n < this.disks.length; ++n) {
            if (!this.disks[n].isFree() && this.disks[n].getObject() instanceof Folder && ((Folder) this.disks[n].getObject()).getLocation().equals(path) && this.disks[n].getBegin()) {
                List.add(this.disks[n]);
            }
        }

        n = 0;

        for (int i = 3 + ProcessManager.executableFileList.size(); i < this.disks.length; ++i) {
            if (!this.disks[i].isFree() && this.disks[i].getObject() instanceof File && ((File) this.disks[i].getObject()).getLocation().equals(path) && this.disks[i].getBegin()) {
                List.add(this.disks[i]);
                ++n;
            }
        }
        return List;
    }

    public Folder getFolder(String path) {
        if (path.equals("C:")) {
            return this.c;
        } else {
            int split = path.lastIndexOf(92);
            String location = path.substring(0, split);
            String folderName = path.substring(split + 1);
            List<Folder> folders = this.getFolders(location);
            Iterator<Folder> iterator = folders.iterator();

            Folder folder;
            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                folder = iterator.next();
            } while (!folder.getName().equals(folderName));

            return folder;
        }
    }

    public Path getPath(String path) {
        var iterator = this.paths.iterator();

        Path p;
        do {
            if (!iterator.hasNext()) {
                return null;
            }

            p = iterator.next();
        } while (!p.getPathName().equals(path));

        return p;
    }

    public int delete(Disk block) {
        // 删除文件
        if (block.getObject() instanceof File) {
            int index;
            // 如果文件已经打开，返回3表示文件未关闭
            if (this.isOpenedFile(block)) return 3;

            File thisFile = (File) block.getObject();
            Folder parent = thisFile.getParent();
            if (parent != null) {
                // 减少父文件夹的目录项数量
                parent.setCatalogNum(parent.getCatalogNum() - 1);
                if (parent.getCatalogNum() % 8 == 0) index = parent.getCatalogNum() / 8;
                else index = parent.getCatalogNum() / 8 + 1;

                // 重新分配块
                this.reallocBlocks(index, this.disks[parent.getDiskNum()]);

                // 从父文件夹中移除子文件
                parent.removeChildren(thisFile);
                // 更新父文件夹的大小
                parent.setSize(getFolderSize(parent));
                // 更新所有祖先文件夹的大小
                while (parent.hasParent()) {
                    parent = parent.getParent();
                    parent.setSize(getFolderSize(parent));
                }
            }

            // 清空与该文件关联的磁盘块
            for (int i = 3 + ProcessManager.executableFileList.size(); i < this.disks.length; ++i) {
                if (!this.disks[i].isFree() && this.disks[i].getObject() instanceof File && this.disks[i].getObject().equals(thisFile)) {
                    this.disks[i].clearBlock();
                }
            }

            // 返回1表示文件删除成功
            return 1;
        }

        // 删除文件夹
        else {
            String folderPath = ((Folder) block.getObject()).getLocation() + "\\" + ((Folder) block.getObject()).getName();
            int index = 0;

            for (int i = 3 + ProcessManager.executableFileList.size(); i < this.disks.length; ++i) {
                if (!this.disks[i].isFree()) {
                    Object obj = this.disks[i].getObject();
                    if (this.disks[i].getType().equals("文件夹")) {
                        if (((Folder) obj).getLocation().equals(folderPath)) {
                            return 2;
                        }
                    } else if (((File) obj).getLocation().equals(folderPath)) {
                        return 2;
                    }

                    if (this.disks[i].getType().equals("文件夹") && this.disks[i].getObject().equals(block.getObject())) {
                        index = i;
                    }
                }
            }

            Folder thisFolder = (Folder) block.getObject();
            Folder parent = thisFolder.getParent();
            if (parent != null) {
                // 减少父文件夹的目录项数量
                parent.setCatalogNum(parent.getCatalogNum() - 1);
                // 从父文件夹中移除子文件夹
                parent.removeChildren(thisFolder);
                // 更新父文件夹的大小
                parent.setSize(getFolderSize(parent));

                int countBlock;
                if (parent.getCatalogNum() % 8 == 0) countBlock = parent.getCatalogNum() / 8;
                else countBlock = parent.getCatalogNum() / 8 + 1;

                // 重新分配块
                this.reallocBlocks(countBlock, this.disks[parent.getDiskNum()]);
            }

            // 移除该文件夹的路径，并清空关联的磁盘块
            this.removePath(this.getPath(folderPath));
            this.disks[index].clearBlock();

            // 返回0表示文件夹删除成功
            return 0;
        }
    }

    public Disk[] getDiskBlocks() {
        return this.disks;
    }

    public int checkNumOfFreeDisk() {
        int num = 0;

        for (Disk disk : this.disks) {
            if (disk.isFree()) {
                ++num;
            }
        }

        return num;
    }

    public int checkNumOfBusyDisk() {
        return DISK_NUM - checkNumOfFreeDisk();
    }

    public void setDiskBlocks(Disk[] disks) {
        this.disks = disks;
    }

    public Disk getBlock(int index) {
        return this.disks[index];
    }

    public ObservableList<File> getOpenedFiles() {
        return openedFiles;
    }

    public void setOpenedFiles(ObservableList<File> openFiles) {
        openedFiles = openFiles;
    }

    public List<Path> getPaths() {
        return this.paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public void addPath(Path path) {
        this.paths.add(path);
    }

    public void removePath(Path path) {
        this.paths.remove(path);
        if (path.hasParent()) {
            path.getParent().removeChildren(path);
        }

    }

    public void replacePath(Path oldPath, String newName) {
        oldPath.setPathName(newName);
    }

    public boolean hasPath(Path path) {
        Iterator<Path> iterator = this.paths.iterator();

        Path p;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            p = iterator.next();
        } while (!p.equals(path));

        return true;
    }

    public boolean hasName(String path, String name) {
        Folder thisFolder = this.getFolder(path);
        var iterator = thisFolder.getChildren().iterator();

        Object child;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            child = iterator.next();
        } while (!child.toString().equals(name));

        return true;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        openedFiles = FXCollections.observableArrayList(new ArrayList<>());
    }

    public static double getSize(int length) {
        return Double.parseDouble(String.valueOf(length));
    }

    public static double getFolderSize(Folder folder) {
        List<Object> children = folder.getChildren();
        double size = 0.0;

        for (Object child : children) {
            if (child instanceof File) {
                size += ((File) child).getSize();
            } else {
                size += getFolderSize((Folder) child);
            }
        }

        return Double.parseDouble(String.valueOf(size));
    }

    public static int blocksCount(int length) {
        if (length <= 64) {
            return 1;
        } else {
            int n;
            if (length % 64 == 0) {
                n = length / 64;
            } else {
                n = length / 64;
                ++n;
            }

            return n;
        }
    }
}
