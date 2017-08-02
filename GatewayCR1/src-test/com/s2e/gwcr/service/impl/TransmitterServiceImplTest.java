package com.s2e.gwcr.service.impl;

import java.io.IOException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.model.BdiFile;
import com.s2e.gwcr.model.BdiFileList;
import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.entity.AbiContext;
import com.s2e.gwcr.model.entity.CertificateItem;
import com.s2e.gwcr.model.entity.CertificateTypeEnum;
import com.s2e.gwcr.model.entity.UserProfile;
import com.s2e.gwcr.repos.DbMock;
import com.s2e.gwcr.service.TransmitterService;

public class TransmitterServiceImplTest {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
//	private static Pack getPackToUpload(BdiFile f) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
//		Pack pack = new Pack();
//		pack.setAbi(new ABI());
//		pack.setRemoteCert(TestRepository.getCert("bdi.crt"));
//		pack.setLocalCert(TestRepository.getCert("s2e.crt"));
//		pack.setLocalPrivateKey(TestRepository.getPrivateKey("s2e.key"));
//		pack.setMedadata(new PackMetadata());
//		pack.setMessageType(PackType.Message);
//		pack.setName(f.getFileName());
//		return pack;
//	}
//	
//	private static Pack getPackToDownload(BdiFile f) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
//
//		Pack pack = new Pack();
//		pack.setAbi(new ABI());
//		pack.setRemoteCert(TestRepository.getCert("bdi.crt"));
//		pack.setLocalCert(TestRepository.getCert("s2e.crt"));
//		pack.setLocalPrivateKey(TestRepository.getPrivateKey("s2e.key"));
//		pack.setMedadata(new PackMetadata());
//		pack.setMessageType(PackType.Communication);
//		pack.setName(f.getFileName());
//		return pack;
//	}
	
	private static BdiEndpoint getEndpoint() throws CertificateException, IOException {		
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
		String method = "List";
		endpoint.setEndpoint(abi.getBdiEndpoint() + "/" + method);
		endpoint.setHttpsCert(httpCert);
		return endpoint;
	}
	
	@Test
	public void testFilelist() throws GwCrException, CertificateException, IOException {
		TransmitterService srv = new TransmitterServiceImpl();
		BdiFileList list = srv.filelist(getEndpoint());
		for (BdiFile file : list.getFiles()) {
			System.out.println(file.getPath() + "/" + file.getFileName());
		}
	}
	

//	@Test
//	public void testUpload() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDownload() {
//		fail("Not yet implemented");
//	}



}
