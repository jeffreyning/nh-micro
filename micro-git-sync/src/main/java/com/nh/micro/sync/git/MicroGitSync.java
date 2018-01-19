package com.nh.micro.sync.git;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * 
 * @author ninghao
 *
 */
public class MicroGitSync {
	private static Logger logger=Logger.getLogger(MicroGitSync.class);
	
	public String localPath;
	public String remotePath;	
	public String branchName = "master";
	public String version=null;
	public String userName;
	public String password;
	public boolean cloneFlag=false;
	public boolean timeFlag=false;
	public boolean openFlag=false;

	public boolean isOpenFlag() {
		return openFlag;
	}
	public void setOpenFlag(boolean openFlag) {
		this.openFlag = openFlag;
	}
	public boolean isTimeFlag() {
		return timeFlag;
	}
	public void setTimeFlag(boolean timeFlag) {
		this.timeFlag = timeFlag;
	}
	public boolean isCloneFlag() {
		return cloneFlag;
	}
	public void setCloneFlag(boolean cloneFlag) {
		this.cloneFlag = cloneFlag;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void initRep() throws Exception{
		if(openFlag==false){
			logger.info("openFlag is false skip git sync");
			return;
		}
		logger.info("begin git sync init remote="+remotePath+" local="+localPath+" branch="+branchName);
		UsernamePasswordCredentialsProvider passwordProvider=null;
		if(userName!=null && !"".equals(userName)){
			passwordProvider=new UsernamePasswordCredentialsProvider(userName,password);	
		}
		if(cloneFlag==true){
			logger.info("begin git sync init clone "+remotePath);
	        File file = new File(localPath);
	        if(file.exists()){
	        	logger.info("begin git sync init deletedir "+localPath);
	            deleteDir(file);
	        }	

	        CloneCommand cc=Git.cloneRepository().setURI(remotePath).setBranch(branchName)
				.setDirectory(new File(localPath));
	        if(passwordProvider!=null){
				cc.setCredentialsProvider(passwordProvider);
	        }
	        Git git=cc.call();
	        logger.info("end git sync init clone ");
		}
        String localRepo=localPath+"/.git";
        Git gitReset = new Git(new FileRepository(localRepo));
        if(version!=null && !"".equals(version)){
        	logger.info("begin git sync init reset version="+version);
        	gitReset.reset().setMode(ResetType.HARD).setRef(version).call();
        	logger.info("end git sync init reset");
        }else{
        	logger.info("skip git sync init reset");
        }
        if(timeFlag==true){
        	
        	Thread thread=new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						Thread.sleep(1000*60);
						reset();
					} catch (Exception e) {
						logger.error("loop git reset error");
					}
					
				}
        		
        	});
        	thread.setName("micro-git-sync-timer");
        	thread.setDaemon(true);
        	thread.run();
        	logger.info("begin git sync timer reset");
        }else{
        	logger.info("skip git sync timer reset");
        }
        logger.info("end git sync init");
		
	}
	public void reset() throws Exception{
		logger.debug("begin git sync loop");
        File file = new File(localPath);
        if(!file.exists()){
            logger.debug("rep not exist");
        }
        String localRepo=localPath+"/.git";
        Git git = new Git(new FileRepository(localRepo));
        git.reset().setMode(ResetType.HARD).call();
        logger.debug("end git sync loop");
	}
	
    private static boolean deleteDir(File dir) {
        if (!dir.exists()) return false;
        if (dir.isDirectory()) {
            String[] childrens = dir.list();

            for (String child : childrens) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) return false;
            }

        }
        return dir.delete();

    }
}
