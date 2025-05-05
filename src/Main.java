import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class Main {
    private record Objet(int valeur, int poids) {}
    private record Result(int poids, List<List<Boolean>> D) {}

    private static Result sac (int nombreObjet, int perturbation, int poidsMinObjet, int poidsMaxObjet, int poidsMaxSac, RandomGenerator rand) {
        List<Objet> objets = createObjectsList(nombreObjet, perturbation, poidsMinObjet, poidsMaxObjet, rand);
        List<Integer> G = createGList(poidsMaxSac);
        List<List<Boolean>> D = createDList(nombreObjet, poidsMaxSac);

        for (int k = 0; k < nombreObjet; ++k) {
            Objet objetK = objets.get(k);
            for (int y = poidsMaxSac; y >= objetK.poids(); --y) {
                if (G.get(y) < G.get(y - objetK.poids()) + objetK.valeur()) {
                    G.set(y, G.get(y - objetK.poids()) + objetK.valeur());
                    D.get(k).set(y, true);
                }
            }
        }

        return new Result(G.get(poidsMaxSac), D);
    }

    private static Result sacGlouton(int nombreObjet, int perturbation, int poidsMinObjet, int poidsMaxObjet, int poidsMaxSac, RandomGenerator rand) {
        List<Objet> objets = createObjectsList(nombreObjet, perturbation, poidsMinObjet, poidsMaxObjet, rand);
        List<Integer> G = createGList(poidsMaxSac);
        List<List<Boolean>> D = createDList(nombreObjet, poidsMaxSac);

        objets.sort((Objet a, Objet b) -> {
            int prioA = a.valeur() / a.poids();
            int prioB = b.valeur() / b.poids();
            return prioB - prioA;
        });

        for (int k = 0; k < objets.size(); ++k) {
            Objet objetK = objets.get(k);
            for (int y = poidsMaxSac; y >= objetK.poids(); --y) {
                G.set(y, G.get(y - objetK.poids()) + objetK.valeur());
                if () {

                }
                D.get(k).set(y, true);
            }
        }

        return null;
    }


    private static List<Objet> createObjectsList(int nombreObjet, int perturbation, int poidsMinObjet, int poidsMaxObjet, RandomGenerator rand){
        ArrayList<Objet> objets = new ArrayList<>();

        for (int i = 0; i < nombreObjet; ++i) {
            int poids = rand.nextInt(poidsMinObjet, poidsMaxObjet + 1);
            int bruit = rand.nextInt(-perturbation, perturbation + 1);
            objets.add(new Objet(poids + bruit, poids));
        }

        return objets;
    }

    private static List<List<Boolean>> createDList(int nombreObjet, int poidsMaxSac) {
        List<List<Boolean>> D = new ArrayList<>();
        for (int i = 0; i < nombreObjet; ++i) {
            D.add(new ArrayList<>(Collections.nCopies(poidsMaxSac + 1, false)));
        }

        return D;
    }

    private static List<Integer> createGList(int poidsMaxSac) {
        return new ArrayList<>(Collections.nCopies(poidsMaxSac + 1, 0));
    }

    public static void main(String[] args) {
        int nombreObjet = 500;
        int perturbationU = 100;
        int poidsMinObjet = 100;
        int poidsMaxObjet = 1000;
        float proportionObjet = 0.25F;
        int poidsMaxSacP = (int) Math.ceil(proportionObjet * nombreObjet * ((double) (poidsMinObjet + poidsMaxObjet) / 2));
        Random rand = new Random();
        rand.setSeed(2012);

        Result result = sac(nombreObjet, perturbationU, poidsMinObjet, poidsMaxObjet, poidsMaxSacP, rand);
        System.out.println(result.poids);
    }
}
