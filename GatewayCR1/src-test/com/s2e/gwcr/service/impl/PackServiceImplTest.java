package com.s2e.gwcr.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.model.PackMetadata;
import com.s2e.gwcr.model.PackType;
import com.s2e.gwcr.model.entity.AbiContext;
import com.s2e.gwcr.repos.DbMock;
import com.s2e.gwcr.test.TUtil;

public class PackServiceImplTest {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	

	private static final String PRJ_DIR = "/Users/davide/git/java/GatewayCR1/";
	private static final String WORK_DIR = PRJ_DIR + "src-test/com/s2e/gwcr/test";


	private static Pack getPackToSend()
			throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		
		Pack pack = new Pack();
		pack.setAbi(new AbiContext());
		pack.setRemoteCert(DbMock.getCert("bdi.crt"));
		pack.setLocalCert(DbMock.getCert("s2e.crt"));
		pack.setLocalPrivateKey(DbMock.getPrivateKey("s2e.key"));
		
		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName("packtosend");

		return pack;
	}

	private static Pack getPackAsBdI()
			throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		Pack pack = new Pack();

		pack.setRemoteCert(DbMock.getCert("s2e.crt"));
		pack.setLocalCert(DbMock.getCert("bdi.crt"));
		pack.setLocalPrivateKey(DbMock.getPrivateKey("bdi.key"));

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName("packtoreceive");

		return pack;
	}

	private static Pack getPackForDecode()
			throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		Pack pack = new Pack();
		pack.setAbi(new AbiContext());

		pack.setRemoteCert(DbMock.getCert("bdi.crt"));
		pack.setLocalCert(DbMock.getCert("s2e.crt"));
		pack.setLocalPrivateKey(DbMock.getPrivateKey("s2e.key"));

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
		pack.setData(DbMock.getData("jmessage.txt"));
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
		packAsBdi.setData(DbMock.getData("jmessage.txt"));
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
		packDec.setData(DbMock.getData("t-bdi.zip.p7e.p7m"));
		transfDec.decode(packDec);
		Files.write(Paths.get(WORK_DIR + "/t-bdi-decoded.zip.p7e.p7m"), diagnosticDec.getP7mBytes());
		Files.write(Paths.get(WORK_DIR + "/t-bdi-decoded.zip.p7e"), diagnosticDec.getP7eBytes());
		Files.write(Paths.get(WORK_DIR + "/t-bdi-decoded.zip"), diagnosticDec.getZipBytes());
		Files.write(Paths.get(WORK_DIR + "/t-bdi-decoded"), packDec.getData());

		TUtil.compare(Paths.get(WORK_DIR + "/t-bdi-decoded"), Paths.get(WORK_DIR + "/t-bdi"));

	}
}
