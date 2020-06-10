package com.harmony.engine.data;

import com.harmony.engine.utils.gameObjects.GameObject;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class DataUtils {

    public static String saveGameObject(GameObject gameObject) {
        String serializedData = "";

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bo);

            os.writeObject(gameObject);
            os.flush();
            serializedData = new String(Base64.getEncoder().encode(bo.toByteArray()));

        } catch(Exception e) {
            e.printStackTrace();
        }

        return serializedData;
    }

    public static GameObject loadGameObject(String data) {
        GameObject gameObject = null;

        try {
            byte[] bytes = Base64.getDecoder().decode(data.getBytes());

            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream si = new ObjectInputStream(bi);
            gameObject = (GameObject) si.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gameObject;
    }

}
