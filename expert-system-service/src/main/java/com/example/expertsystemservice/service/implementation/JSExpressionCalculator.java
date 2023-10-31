package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.service.ExpressionCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Component
@Slf4j
public class JSExpressionCalculator implements ExpressionCalculator {

    private final ScriptEngine engine = scriptEngine();

    private ScriptEngine scriptEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        var factories = manager.getEngineFactories();
        log.info(factories.toString());
        log.info("factories.size(): " + factories.size());
        ScriptEngine engine = factories.getFirst().getScriptEngine();
        log.info(engine.toString());
        return engine;
    }

    @Override
    public String evaluate(String expression) {
        String output = "failed to evaluate";
        try {
            output = engine.eval(expression).toString();
        } catch (ScriptException e) {
            log.error(e.toString());
        }
        return output;
    }

    @Override
    public boolean evaluateToBoolean(String s) {
        return Boolean.parseBoolean(evaluate(s).trim());
    }
}
