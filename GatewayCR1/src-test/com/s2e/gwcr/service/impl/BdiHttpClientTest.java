package com.s2e.gwcr.service.impl;

import static com.s2e.gwcr.repos.DbMock.readCert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.model.PackMetadata;
import com.s2e.gwcr.model.PackType;
import com.s2e.gwcr.model.entity.AbiContext;
import com.s2e.gwcr.repos.DbMock;

import ddc.commons.http.HttpLiteClientResponse;

public class BdiHttpClientTest {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static final String LOCAL_PATH = "/Users/davide/tmp/input";
	private static final String REMOTE_HOST = "http://localhost:8080";

	private static final String FILENAME_DATA = "CRCOM_FT_03205_045120_2017040413151600.output";
	private static final String FILENAME_METADATA = "metadata.json";

	// private static final String REMOTE_HOST="https://tomcat-apache";

	private static Pack getPackToSend() throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
		Pack pack = new Pack();
		pack.setAbi(new AbiContext());
		pack.setRemoteCert(DbMock.readCert("bdi.crt"));
		pack.setLocalCert(DbMock.readCert("s2e.crt"));
		pack.setLocalPrivateKey(DbMock.getPrivateKey("s2e.key"));

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName("packtosend");

		return pack;
	}

	private static BdiEndpoint getListFileEndpoint() throws CertificateException {
		BdiEndpoint endpoint = new BdiEndpoint();
		String url = REMOTE_HOST + "/GatewayCR-BdI/download";
		endpoint.setEndpoint(url);
		endpoint.setHttpsCert(readCert("apache-tomcat.pem"));
		System.out.println("getListFileEndpoint:" + endpoint.getEndpoint());
		return endpoint;
	}

	private static BdiEndpoint getDownloadEndpoint() throws CertificateException {
		BdiEndpoint endpoint = new BdiEndpoint();
		String url = REMOTE_HOST + "/GatewayCR-BdI/download";
		endpoint.setEndpoint(url);
		endpoint.setHttpsCert(readCert("apache-tomcat.pem"));
		System.out.println("getDownloadEndpoint:" + endpoint.getEndpoint());
		return endpoint;
	}

	private static BdiEndpoint getUploadEndpoint() throws CertificateException {
		BdiEndpoint endpoint = new BdiEndpoint();
		String url = REMOTE_HOST + "/GatewayCR-BdI/upload";
		endpoint.setEndpoint(url);
		endpoint.setHttpsCert(readCert("apache-tomcat.pem"));
		System.out.println("getUploadEndpoint:" + endpoint.getEndpoint());
		return endpoint;
	}

	@Test
	public void testUploadFile() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, IOException {

		// String filename = "Comunicazione.pdf";
		// String filename = "t-bdi.zip.p7e.p7m";
		// String filename = "CRCOM_FT_03205_045120_2017040413151600.output";
		String filename = FILENAME_DATA;

		Path path = Paths.get(LOCAL_PATH).resolve(filename);
		byte[] data = DbMock.readFileAsByte(path);

		BdiHttpClient ps = new BdiHttpClient();
		HttpLiteClientResponse res = ps.putData(getUploadEndpoint(), filename, data);

		System.out.println(res);
		System.out.println(res.getBody());
	}

	@Test
	public void testUploadMetadata() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, IOException {
//		String filename = "metadata.json";
		String filename = FILENAME_METADATA;		
		String json = DbMock.readResourceAsString(filename);
		BdiHttpClient ps = new BdiHttpClient();
		HttpLiteClientResponse res = ps.postJson(getUploadEndpoint(), filename, json);

		System.out.println(res);
		System.out.println(res.getBody());
	}

	@Test
	public void testDownload() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, IOException {
		// String filename = "Comunicazione.pdf";
//		String filename = "metadata.json.json";
		String filename = FILENAME_DATA;
		
		BdiHttpClient ps = new BdiHttpClient();
		HttpLiteClientResponse res = ps.getData(getDownloadEndpoint(), filename);
		
		System.out.println(res);
		System.out.println(res.getHeader());
		String remoteFileName = res.getHeader().get("Content-Disposition").split("=")[1].replaceAll("\"", "");
		System.out.println(remoteFileName);
		String outFile = LOCAL_PATH + "/" + remoteFileName;
		System.out.println("writing file:" + outFile);
		FileOutputStream out = new FileOutputStream(outFile);
		out.write(res.getData());
		out.close();
	}

	@Test
	public void testListFile() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, IOException {
		BdiHttpClient ps = new BdiHttpClient();
		BdiEndpoint endpoint = getListFileEndpoint();
		HttpLiteClientResponse res = ps.getString(endpoint);
		System.out.println(res);
		System.out.println(res.getBody());
	}
}