package com.s2e.gwcr.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import org.junit.Test;

import com.s2e.gwcr.model.ABI;
import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.model.PackMetadata;
import com.s2e.gwcr.model.PackType;
import com.s2e.gwcr.test.TUtils;

public class PackTransformerImplTest {
	private static final String WORK_DIR = "/Users/davide/src/eclipse-ws-neon-temp2/GatewayCR1/src-test/com/s2e/gwcr/test";

	private static final File SOURCE_JFILE = new File(WORK_DIR, "jmessage.txt");

	private static final File P7E_JFILE = new File(WORK_DIR, "jmessage.txt.p7e");
	private static final File P7M_JFILE = new File(WORK_DIR, "jmessage.txt.p7e.p7m");

	private static final File P7E_DECRYPTED_JFILE = new File(WORK_DIR, "jmessage.p7e.txt");
	private static final File P7M_DECRYPTED_JFILE = new File(WORK_DIR, "jmessage.p7m.p7e.txt");

	private static final File BDI_CERT = new File(WORK_DIR, "bdi.crt");
	private static final File BDI_KEY = new File(WORK_DIR, "bdi.key");

	private static final File S2E_CERT = new File(WORK_DIR, "s2e.crt");
	private static final File S2E_KEY = new File(WORK_DIR, "s2e.key");

	@Test
	public void testEncodeX509CertificatePrivateKeyPathPath() {
//		Crypto c = new Crypto();
//		c.encode(cert, privateKey, data)
	}
	
	private static Pack getPack() throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		Pack pack = new Pack();
		pack.setAbi(new ABI());
		pack.setData(TUtils.getData("jmessage.txt"));
		
		pack.setRemoteCert(TUtils.getCert("bdi.crt"));
		pack.setLocalCert(TUtils.getCert("s2e.crt"));
		pack.setLocalPrivateKey(TUtils.getPrivateKey("s2e.key"));
		
		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName("packtest1");
		
		return pack;
	}

	@Test
	public void testEncodeX509CertificatePrivateKeyByteArray() throws GwCrException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException, IOException  {
		PackTransformerImpl t = new PackTransformerImpl();
		Pack pack = getPack();
		Map<String, byte[]> map = t.encode_Diagnostic(pack);
		
		Files.write(Paths.get(WORK_DIR + "/encoded.zip"), map.get("zip"));
		Files.write(Paths.get(WORK_DIR + "/encoded.zip.p7e"), map.get("p7e"));
		Files.write(Paths.get(WORK_DIR + "/encoded.zip.p7e.p7m"), map.get("p7m"));
		
	}
//	@Test
//	public void testEncode() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDecode() {
//		fail("Not yet implemented");
//	}

}
