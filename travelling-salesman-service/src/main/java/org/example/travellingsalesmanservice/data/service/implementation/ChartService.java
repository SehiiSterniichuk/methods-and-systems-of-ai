package org.example.travellingsalesmanservice.data.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.data.domain.Statistic;
import org.example.travellingsalesmanservice.data.domain.Task;
import org.example.travellingsalesmanservice.data.repository.TaskRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final TaskRepository repository;

    public ByteArrayOutputStream makeChart(String id) throws IOException {
        List<Statistic> statistics = repository.findById(id)
                .flatMapIterable(Task::getStatisticList)
                .sort(Comparator.comparingInt(Statistic::iteration))
                .collectList().block();
        assert statistics != null;
        XYSeries series = new XYSeries("Path vs Iteration");
        for (Statistic stat : statistics) {
            series.add(stat.iteration(), stat.path());
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Path vs Iteration",
                "Iteration",
                "Path",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);

        // Set line color and thickness
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);

        ByteArrayOutputStream chartImage = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(chartImage, chart, 800, 400);
        return chartImage;
    }
}
