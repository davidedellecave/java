package com.s2e.gwcr.service.impl;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import com.s2e.gwcr.model.ABI;
import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.model.PackMetadata;
import com.s2e.gwcr.model.PackType;
import com.s2e.gwcr.repos.TestRepository;
import com.s2e.gwcr.test.TUtil;

public class PackServiceImplTest {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	

	private static final String PRJ_DIR = "/Users/davide/git/java/GatewayCR1/";
	private static final String WORK_DIR = PRJ_DIR + "src-test/com/s2e/gwcr/test";

//	private static final File SOURCE_JFILE = new File(WORK_DIR, "jmessage.txt");
//
//	private static final File BDI_CERT = new File(WORK_DIR, "bdi.crt");
//	private static final File BDI_KEY = new File(WORK_DIR, "bdi.key");
//
//	private static final File S2E_CERT = new File(WORK_DIR, "s2e.crt");
//	private static final File S2E_KEY = new File(WORK_DIR, "s2e.key");


	public void testEncodeX509CertificatePrivateKeyPathPath() {
		// Crypto c = new Crypto();
		// c.encode(cert, privateKey, data)
	}

	private static Pack getPackToSend()
			throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		
		Pack pack = new Pack();
		pack.setAbi(new ABI());
		pack.setRemoteCert(TestRepository.getCert("bdi.crt"));
		pack.setLocalCert(TestRepository.getCert("s2e.crt"));
		pack.setLocalPrivateKey(TestRepository.getPrivateKey("s2e.key"));
		
//		String url = "https://tomcat-apache/GatewayCR-BdI/List";
//		pack.setEndpoint(url);

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName("packtosend");

		return pack;
	}

	private static Pack getPackAsBdI()
			throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		Pack pack = new Pack();

		pack.setRemoteCert(TestRepository.getCert("s2e.crt"));
		pack.setLocalCert(TestRepository.getCert("bdi.crt"));
		pack.setLocalPrivateKey(TestRepository.getPrivateKey("bdi.key"));

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName("packtoreceive");

		return pack;
	}

	private static Pack getPackForDecode()
			throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		Pack pack = new Pack();
		pack.setAbi(new ABI());

		pack.setRemoteCert(TestRepository.getCert("bdi.crt"));
		pack.setLocalCert(TestRepository.getCert("s2e.crt"));
		pack.setLocalPrivateKey(TestRepository.getPrivateKey("s2e.key"));

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName("packtest1");

		return pack;
	}

	@Test
	public void encodeToSend()
			throws GwCrException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		PackServiceImpl t = new PackServiceImpl();
		EncodeDiagnostic diagnostic = new EncodeDiagnostic();
		t.setDiagnostic(diagnostic);

		Pack pack = getPackToSend();
		pack.setData(TestRepository.getData("jmessage.txt"));
		Files.write(Paths.get(WORK_DIR + "/t-s2e"), pack.getData());
		t.encode(pack);
		Files.write(Paths.get(WORK_DIR + "/t-s2e.zip"), diagnostic.getZipBytes());
		Files.write(Paths.get(WORK_DIR + "/t-s2e.zip.p7e"), diagnostic.getP7eBytes());
		Files.write(Paths.get(WORK_DIR + "/t-s2e.zip.p7e.p7m"), diagnostic.getP7mBytes());
	}

	@Test
	public void encodeAsBDI()
			throws GwCrException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		// ENCODE
		PackServiceImpl transfAsBdi = new PackServiceImpl();
		EncodeDiagnostic diagnosticBdi = new EncodeDiagnostic();
		transfAsBdi.setDiagnostic(diagnosticBdi);

		Pack packAsBdi = getPackAsBdI();
		packAsBdi.setData(TestRepository.getData("jmessage.txt"));
		Files.write(Paths.get(WORK_DIR + "/t-bdi"), packAsBdi.getData());
		transfAsBdi.encode(packAsBdi);
		Files.write(Paths.get(WORK_DIR + "/t-bdi.zip"), diagnosticBdi.getZipBytes());
		Files.write(Paths.get(WORK_DIR + "/t-bdi.zip.p7e"), diagnosticBdi.getP7eBytes());
		Files.write(Paths.get(WORK_DIR + "/t-bdi.zip.p7e.p7m"), diagnosticBdi.getP7mBytes());

		// DECODE
		PackServiceImpl transfDec = new PackServiceImpl();
		EncodeDiagnostic diagnosticDec = new EncodeDiagnostic();
		transfDec.setDiagnostic(diagnosticDec);

		Pack packDec = getPackForDecode();
		packDec.setData(TestRepository.getData("t-bdi.zip.p7e.p7m"));
		transfDec.decode(packDec);
		Files.write(Paths.get(WORK_DIR + "/t-bdi-decoded.zip.p7e.p7m"), diagnosticDec.getP7mBytes());
		Files.write(Paths.get(WORK_DIR + "/t-bdi-decoded.zip.p7e"), diagnosticDec.getP7eBytes());
		Files.write(Paths.get(WORK_DIR + "/t-bdi-decoded.zip"), diagnosticDec.getZipBytes());
		Files.write(Paths.get(WORK_DIR + "/t-bdi-decoded"), packDec.getData());

//		TUtil.compare(Paths.get(WORK_DIR + "/t-bdi-decoded.zip.p7e.p7m"), Paths.get(WORK_DIR + "/t-bdi.zip.p7e.p7m"));
//		TUtil.compare(Paths.get(WORK_DIR + "/t-bdi-decoded.zip.p7e"), Paths.get(WORK_DIR + "/t-bdi.zip.p7e"));
//		TUtil.compare(Paths.get(WORK_DIR + "/t-bdi-decoded.zip"), Paths.get(WORK_DIR + "/t-bdi.zip"));
		TUtil.compare(Paths.get(WORK_DIR + "/t-bdi-decoded"), Paths.get(WORK_DIR + "/t-bdi"));

	}
	// @Test
	// public void testEncode() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testDecode() {
	// fail("Not yet implemented");
	// }

}
