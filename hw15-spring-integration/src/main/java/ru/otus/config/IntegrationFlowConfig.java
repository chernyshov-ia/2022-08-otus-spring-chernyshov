package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.domain.Instruction;
import ru.otus.domain.Task;
import ru.otus.services.InstructionService;
import ru.otus.services.TaskProcessor;

@Configuration
@EnableIntegration
@IntegrationComponentScan(basePackageClasses = TaskProcessor.class)
public class IntegrationFlowConfig {

    private final InstructionService instructionService;

    public IntegrationFlowConfig(InstructionService instructionService) {
        this.instructionService = instructionService;
    }

    @Bean
    public QueueChannel taskChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel resultChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(1000).maxMessagesPerPoll(2).get();
    }


    @Bean
    public IntegrationFlow taskFlow() {
        return IntegrationFlows.from(taskChannel())
                .<Task, Instruction>route(
                        Task::getInstruction,
                        mapping -> mapping
                                .subFlowMapping(
                                        Instruction.UPPERCASE,
                                        sf -> sf.transform(Task::getParams)
                                                .handle(instructionService, "uppercase")
                                                .channel(resultChannel())

                                )
                                .subFlowMapping(
                                        Instruction.REVERSE_WORDS,
                                        sf -> sf.transform(Task::getParams)
                                                .split(f -> f.delimiters(","))
                                                .handle(instructionService, "reverse")
                                                .aggregate()
                                                .channel(resultChannel())
                                )
                )
                .get();
    }
}
