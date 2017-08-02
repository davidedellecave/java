package com.s2e.gwcr.repos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.s2e.gwcr.model.entity.AbiContext;
import com.s2e.gwcr.model.entity.CertificateItem;
import com.s2e.gwcr.model.entity.CertificateTypeEnum;
import com.s2e.gwcr.model.entity.UserAccount;
import com.s2e.gwcr.model.entity.UserProfile;
import com.s2e.gwcr.model.entity.UserRole;
import com.s2e.gwcr.model.entity.UserRoleEnum;

import ddc.util.StringInputStream;

public class DbMock {

	public static List<UserProfile> getUserProfiles() throws IOException {
		List<UserProfile> list = new ArrayList<>();
		UserProfile p = null;
		p = createUserProfile("userprofileA");
		list.add(p);
		p = createUserProfile("userprofileB");
		list.add(p);
		p = createUserProfile("userprofileC");
		list.add(p);
		return list;
	}

	private static CertificateItem createRemoteCert(String alias) throws IOException {
		CertificateItem c = new CertificateItem();
		c.setAlias(alias);
		c.setType(CertificateTypeEnum.remoteCert);
		c.setData(getData("bdi.crt"));
		return c;
	}

	private static CertificateItem createPrivateKey(String alias) throws IOException {
		CertificateItem c = new CertificateItem();
		c.setAlias(alias);
		c.setType(CertificateTypeEnum.localPrivateKey);
		c.setData(getData("s2e.key"));
		return c;
	}

	private static CertificateItem createLocalCert(String alias) throws IOException {
		CertificateItem c = new CertificateItem();
		c.setAlias(alias);
		c.setType(CertificateTypeEnum.localCert);
		c.setData(getData("s2e.crt"));
		return c;
	}

	private static CertificateItem createHttpCert(String alias) throws IOException {
		CertificateItem c = new CertificateItem();
		c.setAlias(alias);
		c.setType(CertificateTypeEnum.remoteHttpCert);
		c.setData(getData("apache-tomcat.pem"));
		return c;
	}

	private static AbiContext createABI(String code) throws IOException {
		AbiContext a = new AbiContext();
		List<CertificateItem> certs = new ArrayList<>();
		certs.add(createRemoteCert("remoteCert-" + code));
		certs.add(createPrivateKey("privateKey-" + code));
		certs.add(createLocalCert("localCert-" + code));
		certs.add(createHttpCert("httpRemoteCert-" + code));
		a.setCertificates(certs);		
		a.setCode(code);
		a.setDescription("Bank " + code);
		a.setBdiEndpoint("https://tomcat-apache/GatewayCR-BdI");
		return a;
	}

	private static UserAccount createUserAccount(String username, String password, UserRoleEnum[] roles) throws IOException {
		UserAccount a = new UserAccount();
		a.setUsername(username);
		a.setPassword(password);
		List<UserRole> roleList = new ArrayList<>();
		for (UserRoleEnum role : roles) {
			UserRole r = new UserRole();
			r.setRoleEnum(role);
			roleList.add(r);
		}
		a.setRoles(roleList);
		return a;
	}

	private static UserProfile createUserProfile(String fullname) throws IOException {
		UserProfile u = new UserProfile();

		List<AbiContext> abis = new ArrayList<>();
		abis.add(createABI("1000"));
		abis.add(createABI("2000"));
		abis.add(createABI("3000"));
		u.setAbis(abis);

		List<UserAccount> accounts = new ArrayList<>();
		accounts.add(createUserAccount("user1", "password1", new UserRoleEnum[] { UserRoleEnum.admin, UserRoleEnum.manager }));
		accounts.add(createUserAccount("user2", "password2", new UserRoleEnum[] { UserRoleEnum.reader }));
		u.setAccounts(accounts);

		List<UserProfile> delegates = new ArrayList<>();
		u.setDelegates(delegates);

		u.setFullname(fullname);
		u.setCcn("fiscal-code");
		u.setEmail("mymail@mail.com");
		u.setPhone("+393423454336");
		return u;
	}

	public static byte[] getData(String name) throws IOException {
		InputStream in = DbMock.class.getResourceAsStream(name);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		return out.toByteArray();
	}

	public static X509Certificate getCert(String name) throws CertificateException {
		InputStream inStream = DbMock.class.getResourceAsStream(name);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (X509Certificate) cf.generateCertificate(inStream);

	}
	
	public static X509Certificate buildCert(byte[] data) throws CertificateException {		
		StringInputStream inStream = new StringInputStream(data);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (X509Certificate) cf.generateCertificate(inStream);

	}

	public static PrivateKey getPrivateKey(String name) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
		byte[] keyBytes = getData(name);

		String temp = new String(keyBytes);
		String privateKeyPEM_base64 = temp.replace("-----BEGIN RSA PRIVATE KEY-----", "");
		privateKeyPEM_base64 = privateKeyPEM_base64.replace("-----END RSA PRIVATE KEY-----", "");
		byte[] privateKeyPlain = org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyPEM_base64);
		PKCS8EncodedKeySpec privateKeyPKCS8 = new PKCS8EncodedKeySpec(privateKeyPlain);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(privateKeyPKCS8);
	}
}
