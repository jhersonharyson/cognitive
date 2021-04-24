package com.example.demo.basedIndex;

import com.intellij.util.indexing.DataIndexer;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.FileBasedIndexExtension;
import com.intellij.util.indexing.ID;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NotNull;

public class MyFileBasedIndex extends FileBasedIndexExtension {
    @Override
    public @NotNull ID getName() {
        return null;
    }

    @Override
    public @NotNull DataIndexer getIndexer() {
        return null;
    }

    @Override
    public @NotNull KeyDescriptor getKeyDescriptor() {
        return null;
    }

    @Override
    public @NotNull DataExternalizer getValueExternalizer() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public FileBasedIndex.@NotNull InputFilter getInputFilter() {
        return null;
    }

    @Override
    public boolean dependsOnFileContent() {
        return false;
    }
}
