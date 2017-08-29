package com.s2e.gwcr.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.model.PackItem;
import com.s2e.gwcr.model.PackMetadata;
import com.s2e.gwcr.model.PackType;
import com.s2e.gwcr.model.entity.AbiContext;
import com.s2e.gwcr.repos.DbMock;

public class PackServiceImplTest {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static final String LOCAL_PATH = "/Users/davide/tmp/input";
	private static final String FILENAME_DATA = "CRCOM_FT_03205_045120_2017040413151600.output";

	
	private static Pack getPackToSend(String filename, byte[] data) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {

		Pack pack = new Pack();
		pack.setAbi(new AbiContext());
		pack.setRemoteCert(DbMock.readCert("bdi.crt"));
		pack.setLocalCert(DbMock.readCert("s2e.crt"));
		pack.setLocalPrivateKey(DbMock.getPrivateKey("s2e.key"));

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName(filename);
		pack.setData(data);

		return pack;
	}

	private static Pack getPackAsBdI(String filename, byte[] data) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		Pack pack = new Pack();

		pack.setRemoteCert(DbMock.readCert("s2e.crt"));
		pack.setLocalCert(DbMock.readCert("bdi.crt"));
		pack.setLocalPrivateKey(DbMock.getPrivateKey("bdi.key"));

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Communication);
		pack.setName(filename);
		pack.setData(data);

		return pack;
	}

	private static Pack getPackForDecode(String filename, byte[] data) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		Pack pack = new Pack();
		pack.setAbi(new AbiContext());

		pack.setRemoteCert(DbMock.readCert("bdi.crt"));
		pack.setLocalCert(DbMock.readCert("s2e.crt"));
		pack.setLocalPrivateKey(DbMock.getPrivateKey("s2e.key"));

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName(filename);
		pack.setData(data);
		return pack;
	}

	@Test
	public void encodeToSend() throws GwCrException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
//		String filename = "jmessage.txt";
		String filename = FILENAME_DATA;
		byte[] data = DbMock.readFileAsByte(Paths.get(LOCAL_PATH).resolve(filename));
		
		Pack pack = getPackToSend(filename, data);

		PackServiceImpl t = new PackServiceImpl();
		EncodeDiagnostic diagnostic = new EncodeDiagnostic();
		t.setDiagnostic(diagnostic);
		t.encode(pack);
		for (PackItem item : diagnostic ) {
			Path path = Paths.get(LOCAL_PATH + "/" + item.getName());
			System.out.println(path);
			Files.write(path, item.getData());	
		}
	}

	@Test
	public void encodeAsBDI() throws GwCrException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		// DECODE AS BDI
		// src
		String filename = FILENAME_DATA;
		byte[] data = DbMock.readFileAsByte(Paths.get(LOCAL_PATH).resolve(filename));
		//target
		String bdiFilename = "bdi-" + filename;	
		Pack packAsBdi = getPackAsBdI(bdiFilename, data);
		
		PackServiceImpl transfAsBdi = new PackServiceImpl();
		EncodeDiagnostic diagnosticBdi = new EncodeDiagnostic();
		transfAsBdi.setDiagnostic(diagnosticBdi);
		transfAsBdi.encode(packAsBdi);
		for (PackItem item : diagnosticBdi ) {
			Path path = Paths.get(LOCAL_PATH + "/" + item.getName());
			System.out.println(path);
			Files.write(path, item.getData());	
		}
	
		// DECODE
		String filenameDecoded = "decoded-" + packAsBdi.getName();
		Pack packDec = getPackForDecode(filenameDecoded, packAsBdi.getData());

		PackServiceImpl transfDec = new PackServiceImpl();
		EncodeDiagnostic diagnosticDec = new EncodeDiagnostic();
		transfDec.setDiagnostic(diagnosticDec);
		transfDec.decode(packDec);
		for (PackItem item : diagnosticDec ) {
			Path path = Paths.get(LOCAL_PATH + "/" + item.getName());
			System.out.println(path);
			Files.write(path, item.getData());	
		}
	}
}
