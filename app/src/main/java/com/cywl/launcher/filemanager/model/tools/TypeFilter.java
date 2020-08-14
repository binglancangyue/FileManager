package com.cywl.launcher.filemanager.model.tools;

public final class TypeFilter {

    public static int getFileType(String path) {
        String ext = path.substring(path.lastIndexOf(".") + 1);
        try {
            switch (ext) {
                //isMusic
                case "mp3":
                case "ogg":
                case "wav":
                case "wma":
                case "m4a":
                case "ape":
                case "dts":
                case "flac":
                case "mp1":
                case "mp2":
                case "aac":
                case "midi":
                case "mid":
                case "mp5":
                case "mpga":
                case "mpa":
                case "m4p":
                case "amr":
                case "m4r":
                    return 1;
                //isVideo
                case "avi":
                case "wmv":
                case "rmvb":
                case "mkv":
                case "m4v":
                case "m1v":
                case "mov":
                case "mpg":
                case "rm":
                case "flv":
                case "pmp":
                case "vob":
                case "dat":
                case "asf":
                case "psr":
                case "3gp":
                case "mpeg":
                case "ram":
                case "divx":
                case "m4b":
                case "mp4":
                case "f4v":
                case "3gpp":
                case "3g2":
                case "3gpp2":
                case "webm":
                case "ts":
                case "tp":
                case "m2ts":
                case "3dv":
                case "3dm":
                case "ogm":
                case "ogv":
                    return 2;
                //isPictrue
                case "png":
                case "jpeg":
                case "jpg":
                case "gif":
                case "bmp":
                case "jfif":
                case "tiff":
                    return 3;
                //isText
                case "txt":
                    return 4;
                //isPdf
                case "pdf":
                    return 5;
                //isWord
                case "doc":
                case "docx":
                    return 6;
                //isExcel
                case "xls":
                case "xlsx":
                    return 7;
                //isPPT
                case "ppt":
                case "pptx":
                    return 8;
                //isHtml32
                case "html":
                case "xml":
                    return 9;
                //isApk
                case "apk":
                    return 10;
                //isISO
                case "iso":
                    return 11;
                default:
                    return -1;
            }
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }

/*    public static int checkFile(String path) {
        String ext = path.substring(path.lastIndexOf(".") + 1);
        try {
            //isMusic
            if (ext.equalsIgnoreCase("mp3")
                    || ext.equalsIgnoreCase("ogg")
                    || ext.equalsIgnoreCase("wav")
                    || ext.equalsIgnoreCase("wma")
                    || ext.equalsIgnoreCase("m4a")
                    || ext.equalsIgnoreCase("ape")
                    || ext.equalsIgnoreCase("dts")
                    || ext.equalsIgnoreCase("flac")
                    || ext.equalsIgnoreCase("mp1")
                    || ext.equalsIgnoreCase("mp2")
                    || ext.equalsIgnoreCase("aac")
                    || ext.equalsIgnoreCase("midi")
                    || ext.equalsIgnoreCase("mid")
                    || ext.equalsIgnoreCase("mp5")
                    || ext.equalsIgnoreCase("mpga")
                    || ext.equalsIgnoreCase("mpa")
                    || ext.equalsIgnoreCase("m4p")
                    || ext.equalsIgnoreCase("amr")
                    || ext.equalsIgnoreCase("m4r")) {
                return 1;
            }
            //isVideo
            if (ext.equalsIgnoreCase("avi")
                    || ext.equalsIgnoreCase("wmv")
                    || ext.equalsIgnoreCase("rmvb")
                    || ext.equalsIgnoreCase("mkv")
                    || ext.equalsIgnoreCase("m4v")
                    || ext.equalsIgnoreCase("m1v")
                    || ext.equalsIgnoreCase("mov")
                    || ext.equalsIgnoreCase("mpg")
                    || ext.equalsIgnoreCase("rm")
                    || ext.equalsIgnoreCase("flv")
                    || ext.equalsIgnoreCase("pmp")
                    || ext.equalsIgnoreCase("vob")
                    || ext.equalsIgnoreCase("dat")
                    || ext.equalsIgnoreCase("asf")
                    || ext.equalsIgnoreCase("psr")
                    || ext.equalsIgnoreCase("3gp")
                    || ext.equalsIgnoreCase("mpeg")
                    || ext.equalsIgnoreCase("ram")
                    || ext.equalsIgnoreCase("divx")
                    || ext.equalsIgnoreCase("m4b")
                    || ext.equalsIgnoreCase("mp4")
                    || ext.equalsIgnoreCase("f4v")
                    || ext.equalsIgnoreCase("3gpp")
                    || ext.equalsIgnoreCase("3g2")
                    || ext.equalsIgnoreCase("3gpp2")
                    || ext.equalsIgnoreCase("webm")
                    || ext.equalsIgnoreCase("ts")
                    || ext.equalsIgnoreCase("tp")
                    || ext.equalsIgnoreCase("m2ts")
                    || ext.equalsIgnoreCase("3dv")
                    || ext.equalsIgnoreCase("3dm")
                    || ext.equalsIgnoreCase("ogm")
                    || ext.equalsIgnoreCase("ogv")) {
                return 2;
            }
            //isPictrue
            if (ext.equalsIgnoreCase("png")
                    || ext.equalsIgnoreCase("jpeg")
                    || ext.equalsIgnoreCase("jpg")
                    || ext.equalsIgnoreCase("gif")
                    || ext.equalsIgnoreCase("bmp")
                    || ext.equalsIgnoreCase("jfif")
                    || ext.equalsIgnoreCase("tiff")) {
                return 3;
            }
            //isText
            if (ext.equalsIgnoreCase("txt")) {
                return 4;
            }
            //isPdf
            if (ext.equalsIgnoreCase("pdf")) {
                return 5;
            }
            //isWord
            if (ext.equalsIgnoreCase("doc")
                    || ext.equalsIgnoreCase("docx")) {
                return 6;
            }
            //isExcel
            if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx")) {
                return 7;
            }
            //isPPT
            if (ext.equalsIgnoreCase("ppt") || ext.equalsIgnoreCase("pptx")) {
                return 8;
            }
            //isHtml32
            if (ext.equalsIgnoreCase("html") || ext.equalsIgnoreCase("xml")) {
                return 9;
            }
            //isApk
            if (ext.equalsIgnoreCase("apk")) {
                return 10;
            }
            //isISO
            if (ext.equalsIgnoreCase("iso")) {
                return 11;
            }
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
        return -1;
    }*/

}
