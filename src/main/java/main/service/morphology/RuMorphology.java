package main.service.morphology;

import main.model.Lemma;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RuMorphology {

    private static LuceneMorphology luceneMorph; //volatile
    static {
        init();
    }
    public static void init(){
        if(luceneMorph == null) {
            try {
                luceneMorph = new RussianLuceneMorphology();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> splitText(String text) {
        return Arrays.stream((text).toLowerCase()
                        .replaceAll("\\p{Punct}", "")
                        .replaceAll("[^а-яё ]", "")
                        .split("\\s"))
                .filter(word -> word.length() != 0)
                .collect(Collectors.toList());
    }

    private static boolean isСontain(String s){
        return s.contains("|n СОЮЗ")
                || s.contains("|l ПРЕДЛ")
                || s.contains("|f МС-П")
                || s.contains("|o МЕЖД")
                || s.contains("|e МС")
                || s.contains("|Z")
                || s.contains("|B");
    }

    public static HashMap<String, Integer> getLem(String text) throws IOException {
        List<String> list = splitText(text);
        HashMap<String, Integer> map = new HashMap<>();
        for(String str : list) {
            if(str.isEmpty() || str.isBlank() || str.length() == 1){
                continue;
            }
            String s = luceneMorph.getMorphInfo(str).get(0);
            if(!isСontain(s)) {
                String key = luceneMorph.getNormalForms(str).get(0); //.getMorphInfo()
                if(map.containsKey(key)) {
                    map.put(key, map.get(key) + 1);
                }
                else {
                    map.put(key, 1);
                }
            }
        }
        return map;
    }

    public static List<String> getsearchLem(String text){
        List<String> list = splitText(text);
        Set<String> lems = new HashSet<>();
        for(String str : list) {
            if(str.isEmpty() || str.isBlank()) {
                continue;
            }
            if(!isСontain(luceneMorph.getMorphInfo(str).get(0))){
                lems.add(luceneMorph.getNormalForms(str).get(0));
            }
        }
        return lems.stream().toList();
    }
    public static String getCorrectQuery(String query, List<Lemma> lemmaList){
        String retQuery = "";
        List<String> list = splitText(query);
        for(String str : list){
            if(str.isEmpty() || str.isBlank()) {
                continue;
            }
            if(!isСontain(luceneMorph.getMorphInfo(str).get(0))){
                String world = luceneMorph.getMorphInfo(str).get(0);
                for(Lemma lemma : lemmaList){
                    if(lemma.getLemma().equals(world)) {
                        retQuery += retQuery.isEmpty() ? str : " " + str;
                    }
                }
            }
        }
        return retQuery;
    }

//    public static List<String> getSourceLem(String text, List<String> lemmaList){
//        List<String> list = splitText(text);
//        HashMap<String, Integer> map = new HashMap<>();
//        for(String str : list) {
//            if(str.isEmpty() || str.isBlank() || str.length() == 1){
//                continue;
//            }
//            String s = luceneMorph.getMorphInfo(str).get(0);
//            if(!isСontain(s)) {
//                String key = luceneMorph.getNormalForms(str).get(0); //.getMorphInfo()
//                if(map.containsKey(key)) {
//                    map.put(key, map.get(key) + 1);
//                }
//                else {
//                    map.put(key, 1);
//                }
//            }
//        }
//        return map;
//    }
}
