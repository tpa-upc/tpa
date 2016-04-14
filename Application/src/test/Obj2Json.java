package test;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by germangb on 11/04/16.
 */
public class Obj2Json {

    public static void main (String[] args) throws IOException {
        ArrayList<Float> position = new ArrayList<>();
        ArrayList<Float> normal = new ArrayList<>();
        ArrayList<Float> uv = new ArrayList<>();

        ArrayList<Float> posData = new ArrayList<>();
        ArrayList<Float> normData = new ArrayList<>();
        ArrayList<Float> uvData = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader("Application/res/quad.obj"));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                String[] spl = line.split(" ");
                position.add(Float.parseFloat(spl[1]));
                position.add(Float.parseFloat(spl[2]));
                position.add(Float.parseFloat(spl[3]));
            } else if (line.startsWith("vn ")) {
                String[] spl = line.split(" ");
                normal.add(Float.parseFloat(spl[1]));
                normal.add(Float.parseFloat(spl[2]));
                normal.add(Float.parseFloat(spl[3]));
            } else if (line.startsWith("vt ")) {
                String[] spl = line.split(" ");
                uv.add(Float.parseFloat(spl[1]));
                uv.add(Float.parseFloat(spl[2]));
            } else if (line.startsWith("f ")) {
                String[] spl = line.split(" ");
                for (int i = 0; i < 3; ++i) {
                    String[] vert = spl[1+i].split("/");
                    int pos = Integer.parseInt(vert[0])-1;
                    int tex = Integer.parseInt(vert[1])-1;
                    int nor = Integer.parseInt(vert[2])-1;
                    posData.add(position.get(pos*3+0));
                    posData.add(position.get(pos*3+1));
                    posData.add(position.get(pos*3+2));

                    normData.add(normal.get(nor*3+0));
                    normData.add(normal.get(nor*3+1));
                    normData.add(normal.get(nor*3+2));

                    uvData.add(uv.get(tex*2+0));
                    uvData.add(uv.get(tex*2+1));

                    indices.add(indices.size());
                }
            }
        }

        reader.close();

        float[] posArray = new float[posData.size()];
        float[] normArray = new float[normData.size()];
        float[] uvArray = new float[uvData.size()];
        int[] indArray = new int[indices.size()];

        for (int i = 0; i < posArray.length; ++i) posArray[i] = posData.get(i);
        for (int i = 0; i < normArray.length; ++i) normArray[i] = normData.get(i);
        for (int i = 0; i < uvArray.length; ++i) uvArray[i] = uvData.get(i);
        for (int i = 0; i < indArray.length; ++i) indArray[i] = indices.get(i);

        AttributeJson posAtt = new AttributeJson();
        posAtt.name = "position";
        posAtt.data = posArray;

        AttributeJson norAtt = new AttributeJson();
        norAtt.name = "normal";
        norAtt.data = normArray;

        AttributeJson uvAtt = new AttributeJson();
        uvAtt.name = "uv";
        uvAtt.data = uvArray;

        IndicesJson indJson = new IndicesJson();
        indJson.type = "uint";
        indJson.data = indArray;

        MeshJson mesh = new MeshJson();
        mesh.attributes = new AttributeJson[] {posAtt, norAtt, uvAtt};
        mesh.primitive = "triangles";
        mesh.indices = indJson;

        Gson gson = new Gson();
        BufferedWriter writ = new BufferedWriter(new FileWriter("Application/res/quad.json"));
        gson.toJson(mesh, writ);
        writ.flush();
        writ.close();
    }

    private static class AttributeJson {
        String name;
        float[] data;
    }

    private static class IndicesJson {
        String type;
        int[] data;
    }

    private static class MeshJson {
        String primitive;
        AttributeJson[] attributes;
        IndicesJson indices;
    }
}
