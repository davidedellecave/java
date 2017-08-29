package com.s2e.gwcr.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.model.BdiFile;
import com.s2e.gwcr.model.BdiFileList;
import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.model.PackMetadata;
import com.s2e.gwcr.model.PackType;
import com.s2e.gwcr.model.entity.AbiContext;
import com.s2e.gwcr.model.entity.CertificateItem;
import com.s2e.gwcr.model.entity.CertificateTypeEnum;
import com.s2e.gwcr.model.entity.UserProfile;
import com.s2e.gwcr.repos.DbMock;
import com.s2e.gwcr.service.TransmitterService;

import ddc.util.Chronometer;

public class TransmitterServiceImplTest {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	private static final String LOCAL_PATH = "/Users/davide/tmp/input";
	private static final String LOCAL_FILENAME_DATA = "CRCOM_FT_03205_045120_2017040413151600.output";
	private static final String REMOTE_FILENAME_DATA = "bdi-CRCOM_FT_03205_045120_2017040413151600.output.zip.p7e.p7m";
	

	
	private static Pack getPackToUpload(String filename) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		UserProfile up = DbMock.getUserProfiles().get(0);
		AbiContext abi = up.getAbis().get(0);
		//get certificates
		byte[] localCertData = null;
		byte[] localPrivateKeyData = null;
		byte[] remoteCertData = null;
		
		for (CertificateItem item : abi.getCertificates()) {
			if (item.getType().equals(CertificateTypeEnum.localCert)) {
				localCertData = item.getData();
			}
			if (item.getType().equals(CertificateTypeEnum.localPrivateKey)) {
				localPrivateKeyData = item.getData();
			}			
			if (item.getType().equals(CertificateTypeEnum.remoteCert)) {
				remoteCertData = item.getData();
			}	
		}
		X509Certificate localCert = DbMock.buildCert(localCertData);
		PrivateKey privateKey = DbMock.getPrivateKey(localPrivateKeyData);
		X509Certificate remoteCert = DbMock.buildCert(remoteCertData);
		
		Pack pack = new Pack();
		pack.setAbi(abi);
		pack.setLocalCert(localCert);
		pack.setLocalPrivateKey(privateKey);
		pack.setRemoteCert(remoteCert);
		PackMetadata meta = new PackMetadata();
		pack.setMedadata(meta);
		pack.setMessageType(PackType.Message);
		pack.setName(filename);
		pack.setPath("");
		return pack;
	}
	
	private static Pack getPackToDownload(String path, String filename) throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {		
		UserProfile up = DbMock.getUserProfiles().get(0);
		AbiContext abi = up.getAbis().get(0);
		//get certificates
		byte[] localCertData = null;
		byte[] localPrivateKeyData = null;
		byte[] remoteCertData = null;
		
		for (CertificateItem item : abi.getCertificates()) {
			if (item.getType().equals(CertificateTypeEnum.localCert)) {
				localCertData = item.getData();
			}
			if (item.getType().equals(CertificateTypeEnum.localPrivateKey)) {
				localPrivateKeyData = item.getData();
			}			
			if (item.getType().equals(CertificateTypeEnum.remoteCert)) {
				remoteCertData = item.getData();
			}	
		}
		X509Certificate localCert = DbMock.buildCert(localCertData);
		PrivateKey privateKey = DbMock.getPrivateKey(localPrivateKeyData);
		X509Certificate remoteCert = DbMock.buildCert(remoteCertData);
		
		Pack pack = new Pack();
		pack.setAbi(abi);
		pack.setLocalCert(localCert);
		pack.setLocalPrivateKey(privateKey);
		pack.setRemoteCert(remoteCert);
		PackMetadata meta = new PackMetadata();
		pack.setMedadata(meta);
		pack.setMessageType(PackType.Communication);
		pack.setName(filename);
		pack.setPath(path);
		return pack;
	}

	private static BdiEndpoint getEndpoint(String method) throws CertificateException, IOException {		
		UserProfile up = DbMock.getUserProfiles().get(0);
		AbiContext abi = up.getAbis().get(0);
		//get http certificate
		byte[] httpCertData = null;
		for (CertificateItem item : abi.getCertificates()) {
			if (item.getType().equals(CertificateTypeEnum.remoteHttpCert)) {
				httpCertData = item.getData();
			}
		}
		X509Certificate httpCert = DbMock.buildCert(httpCertData);
		BdiEndpoint endpoint = new BdiEndpoint();
		endpoint.setEndpoint(abi.getBdiEndpoint() + "/" + method);
		endpoint.setHttpsCert(httpCert);
		return endpoint;
	}
	

	
	@Test
	public void testDownloadFile() throws GwCrException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		Chronometer chron = new Chronometer();
		TransmitterService srv = new TransmitterServiceImpl();
		System.out.println("downloading..." + REMOTE_FILENAME_DATA);
		Pack pack = getPackToDownload("", REMOTE_FILENAME_DATA);
		srv.download(getEndpoint("download"), pack);
		System.out.println(pack.toString());
		System.out.println("Elapsed " + chron.toString());
		
		//save localfile
		Path localFile = Paths.get(LOCAL_PATH).resolve(pack.getName());
		System.out.println("Save local " + localFile.toString());
		DbMock.writeFile(localFile, pack.getData());
	}
	
	@Test
	public void testDownloadList() throws GwCrException, CertificateException, IOException {
		TransmitterService srv = new TransmitterServiceImpl();
		BdiFileList list = srv.listFile(getEndpoint("download"));
		for (BdiFile file : list.getFiles()) {
			System.out.println(file.toString());
		}
	}
	
	@Test
	public void testUploadFile() throws GwCrException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		TransmitterService srv = new TransmitterServiceImpl();		
		Pack pack = getPackToUpload(LOCAL_FILENAME_DATA);
		
		Path path = Paths.get(LOCAL_PATH).resolve(pack.getName());
		pack.setData(DbMock.readFileAsByte(path));
		srv.upload(getEndpoint("upload"), pack);
		System.out.println(pack.toString());
	}
	





}
