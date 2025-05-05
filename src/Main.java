import java.util.*;
import java.util.random.RandomGenerator;

// TODO changer les List par des Arrays

public class Main {
    private record Objet(int valeur, int poids, int indice) {}
    private record Result(int poids, boolean[] objets) {}

    private static Result sac(int nombreObjet, int perturbation, int poidsMinObjet, int poidsMaxObjet, int poidsMaxSac, RandomGenerator rand) {
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

        boolean[] objetsPris = new boolean[nombreObjet];

        int poidsRestant = G.get(poidsMaxSac);
        for (int k = nombreObjet - 1; k >= 0; --k) {
            if (D.get(k).get(poidsRestant)) {
                objetsPris[k] = true;
                poidsRestant -= objets.get(k).poids();
            } else {
                objetsPris[k] = false;
            }
        }

        return new Result(G.get(poidsMaxSac), objetsPris);
    }

    private static Result sacGlouton(int nombreObjet, int perturbation, int poidsMinObjet, int poidsMaxObjet, int poidsMaxSac, RandomGenerator rand) {
        List<Objet> objets = createObjectsList(nombreObjet, perturbation, poidsMinObjet, poidsMaxObjet, rand);

        objets.sort((Objet a, Objet b) -> {
            float prioB = (float) b.valeur() / b.poids();
            float prioA = (float) a.valeur() / a.poids();

            if (prioB > prioA) return 1;
            else if (prioA > prioB) return -1;

            return 0;
        });

        boolean[] objetsPris = new boolean[nombreObjet];
        int poidsRestant = poidsMaxSac;
        for (Objet objet : objets) {
            if (poidsRestant >= objet.poids()) {
                poidsRestant -= objet.poids();
                objetsPris[objet.indice()] = true;
            } else {
                objetsPris[objet.indice()] = false;
            }
        }

        return new Result(poidsMaxSac - poidsRestant, objetsPris);
    }


    private static List<Objet> createObjectsList(int nombreObjet, int perturbation, int poidsMinObjet, int poidsMaxObjet, RandomGenerator rand){
        ArrayList<Objet> objets = new ArrayList<>();

        for (int i = 0; i < nombreObjet; ++i) {
            int poids = rand.nextInt(poidsMinObjet, poidsMaxObjet + 1);
            int bruit = rand.nextInt(-perturbation, perturbation + 1);
            objets.add(new Objet(poids + bruit, poids, i));
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
