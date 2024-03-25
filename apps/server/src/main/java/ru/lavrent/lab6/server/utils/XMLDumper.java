package ru.lavrent.lab6.server.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.lavrent.lab6.common.exceptions.ValidationException;
import ru.lavrent.lab6.common.models.Coordinates;
import ru.lavrent.lab6.common.models.Difficulty;
import ru.lavrent.lab6.common.models.Discipline;
import ru.lavrent.lab6.common.models.LabWork;
import ru.lavrent.lab6.server.exceptions.DeserializationException;
import ru.lavrent.lab6.server.exceptions.SerializationException;
import ru.lavrent.lab6.server.interfaces.IDumper;
import ru.lavrent.lab6.server.managers.CollectionManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.util.function.Function;

public class XMLDumper implements IDumper {
  private File file;
  private PrintWriter printWriter;
  private CollectionManager collectionManager;

  public XMLDumper(String filePath, CollectionManager collectionManager)
      throws AccessDeniedException {
    this.collectionManager = collectionManager;
    file = Paths.get(filePath).toFile();
    if (!file.exists()) {
      try {
        this.createDbFile(filePath);
        collectionManager.saveToFile(filePath);
        System.out.println("created new db in " + filePath);
      } catch (IOException e) {
      }
    }
    if (!file.canRead()) {
      throw new AccessDeniedException("file %s is not readable".formatted(filePath));
    }
    if (!file.canWrite()) {
      throw new AccessDeniedException("file %s is not writable".formatted(filePath));
    }
  }

  private File createDbFile(String filePath) throws IOException {
    File file = new File(filePath);
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.close();
    return file;
  }

  private static void addTextNode(Document doc, Element rootElement, String tag, String content) {
    Element name = doc.createElement(tag);
    name.appendChild(doc.createTextNode(content));
    rootElement.appendChild(name);
  }

  private static String getTextFromNode(Element rootElement, String tag) {
    return rootElement.getElementsByTagName(tag).item(0).getTextContent();
  }

  private static String toStringNullable(Object x) {
    return x == null ? "" : x.toString();
  }

  private static <T> T parse(String s, Function<String, T> fn) {
    return s.isEmpty() ? null : fn.apply(s);
  }

  private static String toXML(Iterable<LabWork> labWorks,
      String type,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) throws SerializationException {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      Element rootElement = doc.createElement("collectionDump");
      doc.appendChild(rootElement);

      addTextNode(doc, rootElement, "type", type);
      addTextNode(doc, rootElement, "createdAt", createdAt.toString());
      addTextNode(doc, rootElement, "updatedAt", createdAt.toString());

      Element collection = doc.createElement("collection");
      for (LabWork labWork : labWorks) {
        Element labWorkElement = doc.createElement("labwork");
        collection.appendChild(labWorkElement);

        addTextNode(doc, labWorkElement, "id", toStringNullable(labWork.getId()));
        addTextNode(doc, labWorkElement, "name", labWork.getName());
        addTextNode(doc, labWorkElement, "creationDate", toStringNullable(labWork.getCreationDate()));

        Element coordinatesElement = doc.createElement("coordinates");
        addTextNode(doc, coordinatesElement, "x", toStringNullable(labWork.getCoordinates().getX()));
        addTextNode(doc, coordinatesElement, "y", toStringNullable(labWork.getCoordinates().getY()));
        labWorkElement.appendChild(coordinatesElement);

        addTextNode(doc, labWorkElement, "minimalPoint", toStringNullable(labWork.getMinimalPoint()));
        addTextNode(doc, labWorkElement, "difficulty", toStringNullable(labWork.getDifficulty().ordinal()));

        Element disciplineElement = doc.createElement("discipline");
        addTextNode(doc, disciplineElement, "name", labWork.getDiscipline().getName());
        addTextNode(doc, disciplineElement, "lectureHours",
            toStringNullable(labWork.getDiscipline().getLectureHours()));
        addTextNode(doc, disciplineElement, "practiceHours",
            toStringNullable(labWork.getDiscipline().getPracticeHours()));
        addTextNode(doc, disciplineElement, "labsCount", toStringNullable(labWork.getDiscipline().getLabsCount()));
        labWorkElement.appendChild(disciplineElement);
      }
      rootElement.appendChild(collection);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.transform(source, result);
      return writer.toString();
    } catch (ParserConfigurationException | TransformerException e) {
      throw new SerializationException(e);
    }
  }

  public void dump()
      throws IOException, SerializationException {
    try {
      printWriter = new PrintWriter(file);
    } catch (FileNotFoundException e) {
      System.out.println("file %s not found, creating".formatted(file.getAbsolutePath()));

      this.file = createDbFile(file.getAbsolutePath());
    }
    String xml = toXML(collectionManager.getList(), collectionManager.getType(), collectionManager.getCreatedAt(),
        collectionManager.getUpdatedAt());
    printWriter.println(xml);
    printWriter.close();
  }

  public void load() throws FileNotFoundException, DeserializationException {
    Scanner scanner = new Scanner(file);
    long lastId = 1;
    try {
      StringBuilder content = new StringBuilder();
      while (scanner.hasNextLine()) {
        content.append(scanner.nextLine()).append("\n");
      }

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new InputSource(new StringReader(content.toString())));

      Element rootElement = document.getDocumentElement();
      if (!rootElement.getTagName().equals("collectionDump")) {
        throw new DeserializationException("root element must be collectionDump");
      }
      String type = getTextFromNode(rootElement, "type");
      LocalDateTime createdAt = parse(getTextFromNode(rootElement, "createdAt"), LocalDateTime::parse);
      LocalDateTime updatedAt = parse(getTextFromNode(rootElement, "updatedAt"), LocalDateTime::parse);

      NodeList labworkNodes = rootElement.getElementsByTagName("labwork");
      for (int i = 0; i < labworkNodes.getLength(); i++) {
        Node labworkNode = labworkNodes.item(i);
        if (labworkNode.getNodeType() != Node.ELEMENT_NODE)
          continue;
        Element labworkElement = (Element) labworkNode;
        long id = parse(getTextFromNode(labworkElement, "id"), Long::parseLong);
        String name = getTextFromNode(labworkElement, "name");

        Element coordinatesElement = (Element) labworkElement.getElementsByTagName("coordinates").item(0);
        long x = parse(getTextFromNode(coordinatesElement, "x"), Long::parseLong);
        int y = parse(getTextFromNode(coordinatesElement, "y"), Integer::parseInt);
        Coordinates coordinates = new Coordinates(x, y);

        ZonedDateTime creationDate = ZonedDateTime.parse(getTextFromNode(labworkElement, "creationDate"));
        long minimalPoint = parse(getTextFromNode(labworkElement, "minimalPoint"), Long::parseLong);
        int difficultyOrd = parse(getTextFromNode(labworkElement, "difficulty"), Integer::parseInt);
        Difficulty difficulty = Difficulty.values()[difficultyOrd];

        Element disciplineElement = (Element) labworkElement.getElementsByTagName("discipline").item(0);
        String disciplineName = getTextFromNode(disciplineElement, "name");
        long lectureHours = parse(getTextFromNode(disciplineElement, "lectureHours"), Long::parseLong);
        Long practiceHours = parse(getTextFromNode(disciplineElement, "practiceHours"), Long::parseLong);
        Integer labsCount = parse(getTextFromNode(disciplineElement, "labsCount"), Integer::parseInt);
        Discipline discipline = new Discipline(disciplineName, lectureHours, practiceHours, labsCount);

        if (collectionManager.getById(id) != null) {
          throw new ValidationException("labwork id %d already exists".formatted(id));
        }
        lastId = Math.max(lastId, id + 1);
        collectionManager.add(new LabWork(id, name, coordinates, creationDate, minimalPoint, difficulty, discipline));
      }
      collectionManager.setMetaData(type, createdAt, updatedAt, lastId);
    } catch (ValidationException | ParserConfigurationException | SAXException | IOException e) {
      throw new DeserializationException(e);
    } finally {
      scanner.close();
    }
  }
}
