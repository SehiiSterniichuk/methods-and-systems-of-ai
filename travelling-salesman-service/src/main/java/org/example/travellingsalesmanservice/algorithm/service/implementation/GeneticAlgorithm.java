package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.example.travellingsalesmanservice.algorithm.domain.Result;
import org.example.travellingsalesmanservice.algorithm.service.*;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;

import java.util.ArrayList;
import java.util.List;

import static org.example.travellingsalesmanservice.app.domain.ResultResponse.*;

@RequiredArgsConstructor
@Slf4j
@Builder
class GeneticAlgorithm implements TravellingSalesmanSolver {//клас відповідальний за роботу алгоритму
    private final PathLengthEstimator estimator;
    private final XYPopulationGenerator generator;
    private final CrossoverAlgorithmFactory crossoverAlgorithmFactory;
    private final TrackingEntity entity;
    private final int[] pathLengths;
    private final Dataset dataset;
    private final TaskConfig taskConfig;

    @Override
    public TrackingEntity start() {//старт алгоритму
        if (isSimple(dataset)) {//перевіряємо чи датасет більше 3 точок
            handleSimpleTask();
            return entity;
        }
        //генеруємо початкову популяцію
        var chromosomes = generator.generateChromosomes(dataset, taskConfig.populationSize());
        //оцінюємо початкову популяцію
        estimator.calculateSquaredPathLength(chromosomes, pathLengths);
        try {
            //переходимо до циклу
            start(chromosomes, crossoverAlgorithmFactory.getCrossover(taskConfig, pathLengths, chromosomes));
        } catch (RuntimeException e) {
            entity.put(getErrorResult(e.getMessage(), -1));
            throw e;
        }
        return entity;
    }

    public static boolean isSimple(Dataset dataset) {
        return dataset.data().length < 4;
    }

    static class SavedPopulation {
        private final List<Chromosome> list;
        private int bestP = -1;
        public final int limit;

        public SavedPopulation(int limit) {
            this.limit = limit;
            this.list = new ArrayList<>(limit + 1);
        }

        public void add(Chromosome chromosome) {
            bestP = (bestP + 1) % limit;
            if (list.size() < limit) {
                list.add(chromosome);
            } else {
                list.set(bestP, chromosome);
            }
        }

        public void setCurrent(Chromosome chromosome){
            if (list.size() < (limit + 1)) {
                list.add(chromosome);
            } else {
                list.set(limit, chromosome);
            }
        }

        public void copyTo(Chromosome chromosomes) {
            chromosomes.insertList(list);
        }
    }

    protected void start(Chromosome chromosomes, CrossoverAlgorithm crossover) {
        int counterOfSameResults = 0;//лічильник не кращих результатів
        Result bestResult = findBestPath(chromosomes, pathLengths);//шукаємо початковий найкоротший шлях
        int i = 0;
        SavedPopulation saved = initSaved();
        saved.add(bestResult.toChromosome());
        for (; i < taskConfig.iterationNumber(); i++) {//поки не закінчилися ітерації продовжуємо цикл
            //проводимо кросовер
            saved.copyTo(chromosomes);
            crossover.performCrossover(chromosomes, pathLengths, taskConfig.mutationProbability());
            //оцінка популяції
            estimator.calculateSquaredPathLength(chromosomes, pathLengths);
            //знаходимо найкращого в поточній популяції
            var current = findBestPath(chromosomes, pathLengths);
            if (current.isBetterThan(bestResult)) {//якщо поточний кращий за останній найкращий
                bestResult = current;//зберігаємо поточний як найкращий
                saved.add(bestResult.toChromosome());
                counterOfSameResults = 0;//обнуляємо лічильник однакового результату
                entity.put(getNewBestResult(bestResult, i));//кладемо результат у чергу як найкращий щоб повідомити користувача
            } else if (counterOfSameResults < taskConfig.allowedNumberOfGenerationsWithTheSameResult()) {
                //якщо результат не краще і ми ще не досягнули ліміту повторень одного й того ж результату
                counterOfSameResults++;//інкрементуємо лічильник
                saved.setCurrent(current.toChromosome());
            } else {//якщо ми не знайшли новий кращий результат і вже досягнули ліміту
                entity.put(getResultCounter(bestResult, i, counterOfSameResults));//кладемо у чергу поточний результат
                break;//перериваємо цикл
            }
            boolean show = i % taskConfig.showEachIterationStep() == 0;
            boolean finish = i + 1 == taskConfig.iterationNumber();
            if (show && !finish) {// якщо це не остання ітерація, але треба повідомити користувача
                entity.put(getShowResult(bestResult, i));//сповіщаємо користувача про поточний номер ітерації
            } else if (finish) {
                entity.put(getFinishResult(bestResult, i));// якщо це остання ітерація, сповіщаємо користувача про це
            }
        }
    }

    private SavedPopulation initSaved() {
        return new SavedPopulation(pathLengths.length / 3);
    }

    private void handleSimpleTask() {
        Result result = Result.builder()
                .path(dataset.data())
                .pathLength(calculatePath(dataset.data()))
                .build();
        entity.put(getFinishResult(result, 0));
    }

    private double calculatePath(Point[] data) {//калькулятор простих кейсів датасету
        if (data.length <= 1) {
            return 0;
        } else if (data.length == 2) {
            return data[0].distance(data[1]);
        } else if (data.length == 3) {
            return data[0].distance(data[1]) + data[1].distance(data[2]) + data[2].distance(data[0]);
        } else {
            throw new UnsupportedOperationException("It's for small sizes");
        }
    }

    //шукаємо найкращий результат у поточному датасеті
    private Result findBestPath(Chromosome chromosomes, int[] pathLengths) {
        int numberOfCities = (chromosomes.x().length - 1) / pathLengths.length;
        int min = pathLengths[0];
        int minIndex = 0;
        for (int i = 1; i < pathLengths.length; i++) {
            int path = pathLengths[i];
            if (path < min) {
                min = path;
                minIndex = i;
            }
        }
        Point[] bestPath = new Point[numberOfCities];
        int index = minIndex * numberOfCities;
        for (int i = 0; i < numberOfCities; i++, index++) {
            bestPath[i] = new Point(chromosomes.x()[index], chromosomes.y()[index]);
        }
        Chromosome best = Chromosome.ofLength(numberOfCities);
        best.fillWith(0, chromosomes, minIndex * numberOfCities, numberOfCities);
        return new Result(bestPath, Math.sqrt(min));
    }
}
