package com.samlic.rpc.arch.codec;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.samlic.rpc.arch.SamlicException;

public class JavaObjectSerializerFactory implements SerializerFactory {

	@Override
	public <T> Serializer<T> getSerializer() {
		return new BinarySerializer<T>() {
			@Override
			public byte[] serialize(T data) {				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();				
				ObjectOutputStream oos = null;
				try {
					oos = new ObjectOutputStream(new BufferedOutputStream(bos));
					oos.writeObject(data);	
					oos.flush();
					return bos.toByteArray();
				} catch(IOException e) {
					throw new SamlicException("Serialize error.", e);
				} finally {
					try {
						if(oos != null) {
							oos.close();
						}
					} catch (IOException e) {
						//do nothing
					}
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public T deserialize(byte[] data) {
				ObjectInputStream ois = null;
				try{
					ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
					return (T)ois.readObject();
				} catch(IOException | ClassNotFoundException e) {
					throw new SamlicException("Deserialize error.", e);
				} finally {
					try {
						if(ois != null) {
							ois.close();
						}
					} catch (IOException e) {
						//do nothing
					}
				}
			}
			
		};
	}

}
