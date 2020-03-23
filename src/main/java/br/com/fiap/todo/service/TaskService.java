package br.com.fiap.todo.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.fiap.todo.domain.Task;
import br.com.fiap.todo.repository.TaskRepository;
import br.com.fiap.todo.web.rest.TaskResource;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

@Service
public class TaskService {
	
	private static final String ENTITY_NAME = "Task";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
	
    @Autowired
    private final TaskRepository taskRepository;
    
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public TaskRepository getRepository() {
    	return this.taskRepository;
    }
    
    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    
    public ResponseEntity<Task> createTask(Task task) throws URISyntaxException {
	    log.debug("REST request to save Task : {}", task);
	    Task result = taskRepository.save(task);
	    return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
	        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
	        .body(result);
    }
    
    public ResponseEntity<Task> updateTask(Task task) throws URISyntaxException {
        log.debug("REST request to update Task : {}", task);
        Task result = taskRepository.save(task);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, task.getId().toString()))
            .body(result);
    }
    
    public ResponseEntity<Task> toggleTask(Long id) throws URISyntaxException {
        log.debug("REST request to toggle Task : {}", id);
        Task myTask = taskRepository.findById(id).orElse(null);
        myTask.isCompleted(!myTask.isIsCompleted());
        Task modifiedTask = taskRepository.save(myTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modifiedTask.getId().toString()))
            .body(modifiedTask);
    }
    
    public List<Task> getAllTasks() {
	    log.debug("REST request to get all Tasks");
	    return taskRepository.findAll();
    }
    public ResponseEntity<Task> getTask(Long id) {
	    log.debug("REST request to get Task : {}", id);
	    Optional<Task> task = taskRepository.findById(id);
	    return ResponseUtil.wrapOrNotFound(task);
    }
    
    public ResponseEntity<Void> deleteTask(Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
    
    
    
    

}
