import ru.leti.wise.task.plugin.graph.GraphCharacteristic;
import ru.leti.wise.task.graph.model.Graph;
import ru.leti.wise.task.graph.model.Vertex;
import ru.leti.wise.task.graph.model.Edge;
import ru.leti.wise.task.plugin.graph.GraphProperty;

import java.util.*;

public class ContentsInducedSubgraph_K22 implements GraphProperty
{
    @Override
    public boolean run(Graph graph)
    {
        List<Vertex> vertices = graph.getVertexList();
        List<Edge> edges = graph.getEdgeList();

        // Построение списка смежности (используется ID вершины)
        Map<Integer, Set<Integer>> adj = new HashMap<>();

        // Инициализация множеств для всех вершин
        for(Vertex v : vertices)
        {
            adj.put(v.getId(), new HashSet<>());
        }

        // Заполнение списка смежности
        for(Edge e : edges)
        {
            int sourceId = e.getSource();
            int targetId = e.getTarget();

            // Добавление в обоих направлениях
            adj.get(sourceId).add(targetId);
            adj.get(targetId).add(sourceId);
        }

        // Поиск индуцированных циклов длины 4
        for(int i = 0; i < vertices.size(); i++)
        {
            Vertex uVertex = vertices.get(i);
            int u = uVertex.getId();

            // Пропуск, если у u есть петля
            if(adj.get(u).contains(u))
                continue;

            for(int j = i + 1; j < vertices.size(); j++)
            {
                Vertex vVertex = vertices.get(j);
                int v = vVertex.getId();

                // Пропуск, если вершины смежные или у v есть петля
                if(adj.get(u).contains(v) || adj.get(v).contains(v))
                    continue;

                // Поиск общих соседей
                Set<Integer> common = new HashSet<>(adj.get(u));
                common.retainAll(adj.get(v));

                // Если количество общих соседей меньше 2, то построить цикл длины 4 невозможно
                if(common.size() < 2)
                    continue;

                // Преобразование в список для индексированного доступа
                List<Integer> commonList = new ArrayList<>(common);

                // Перебор пар общих соседей
                for(int k = 0; k < commonList.size(); k++)
                {
                    int w = commonList.get(k);

                    // Пропуск, если у w есть петля
                    if(adj.get(w).contains(w))
                        continue;

                    for(int l = k + 1; l < commonList.size(); l++)
                    {
                        int z = commonList.get(l);

                        // Проверка, что w и z не смежны, и у z нет петли
                        if(!adj.get(w).contains(z) && !adj.get(z).contains(z))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
