package io.github.UmbrellaLeaf5.synth.core;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfiguration
@ComponentScan
@EnableScheduling
@ConfigurationPropertiesScan
public class SyntheticHumanCoreStarterConfiguration {}
