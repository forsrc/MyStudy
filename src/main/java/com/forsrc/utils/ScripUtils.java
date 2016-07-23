package com.forsrc.utils;

import org.stathissideris.ascii2image.core.FileUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;

public class ScripUtils {
    public static <RTN> RTN run(ScripName name, String src, ScripUtilsAdapter adapter)
            throws IOException, ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(name.getName());
        engine.eval(src);
        ScripConfig config = new ScripConfig();
        adapter.todo(engine, config);
        RTN rtn = null;
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;
            rtn = (RTN) invoke.invokeFunction(config.getFuncName(), config.getArgs());
        }
        return rtn;
    }

    public static <RTN> RTN run(ScripName name, File src, ScripUtilsAdapter adapter)
            throws IOException, ScriptException, NoSuchMethodException {
        return run(name, FileUtils.readFile(src, "UTF-8"), adapter);
    }

    public static <RTN> RTN run(ScripName name, File src, final String funcName, final Object... args)
            throws IOException, ScriptException, NoSuchMethodException {

        return ScripUtils.run(ScripName.JAVASCRIPT,
                src,
                new ScripUtilsAdapter() {

                    @Override
                    public void todo(ScriptEngine scriptEngine, ScripConfig config) {
                        config.setFuncName(funcName);
                        config.setArgs(args);
                    }

                });
    }

    public static enum ScripName {
        JAVASCRIPT("javascript"), GROOVY("groovy");
        private String name;

        private ScripName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface ScripUtilsAdapter<RTN> {
        public void todo(ScriptEngine scriptEngine, ScripConfig config);
    }

    public static class ScripConfig {
        private String funcName;
        private Object[] args;

        public ScripConfig() {

        }

        public ScripConfig(String funcName, Object[] args) {
            super();
            this.funcName = funcName;
            this.args = args;
        }

        public String getFuncName() {
            return this.funcName;
        }

        public void setFuncName(String funcName) {
            this.funcName = funcName;
        }

        public Object[] getArgs() {
            return this.args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

    }

}
